const TYPE_COMMAND_ROOM_ENTER = "enterRoom";
const TYPE_COMMAND_ROOM_LIST = "roomList";
const TYPE_COMMAND_DIALOGUE = "dialogue";
const TYPE_COMMAND_READY = "ready";
const TYPE_COMMAND_OFFER = "offer";
const TYPE_COMMAND_ANSWER = "answer";
const TYPE_COMMAND_CANDIDATE = "candidate";

let iceServers = {
  "iceServers": [
    {"url": "stun:stun.services.mozilla.com"},
    {"url": "stun:stun.l.google.com:19302"}
  ]
};
const mediaConstraints = {
  video: {width: 500, height: 500},
  audio: false // true,//由于没有麦克风，所有如果请求音频，会报错，不过不会影响视频流播放
};

const screenConstraints = {
  video: {
    cursor: 'always' | 'motion' | 'never',
    displaySurface: 'application' | 'browser' | 'monitor' | 'window'
  }
}

const offerOptions = {
  iceRestart: true,
  offerToReceiveAudio: false, //true,由于没有麦克风，所有如果请求音频，会报错，不过不会影响视频流播放
  offerToReceiveVideo: true
};

let localMediaStream;

let rtcPeerConnection;

//写在这里不行，得写到promise的then里面才起作用，暂不知为何
//const localVideo = document.getElementById("localVideo");
//const remoteVideo = document.getElementById("remoteVideo");

let websocket;
let userId = randomName();
let roomId;
let caller = false;

let log;

window.onload = () => {
  //初始化log
  log = (message) => {
    let log = document.getElementById("logContent");
    let oneLog = document.createElement("span");
    oneLog.innerText = message;
    let br = document.createElement("br");
    log.append(oneLog, br);
  };

  //设定随机名称
  document.getElementById("showUserId").innerText = userId;

  //初始化websocket连接
  get("/getWebSocketUrl")
  .then((data) => {
    if (!websocket) {
      websocket = new WebSocket(data.url);
      log("websocket连接成功")
    }
    websocket.onopen = () => {
      websocket.send(JSON.stringify({command: TYPE_COMMAND_ROOM_LIST}))
    };
    websocket.onclose = () => {
      log("Connection closed.");
    };
    websocket.onerror = () => {
      log("websocket error");
    };
    websocket.onmessage = handleMessage;

  })
  .catch((error) => {
    log(error);
  });

  //初始化各种按钮
  document.getElementById("enterRoom").onclick = () => {
    roomId = document.getElementById("roomId").value;
    websocket.send(JSON.stringify(
        {command: TYPE_COMMAND_ROOM_ENTER, userId: userId, roomId: roomId}));
    //不用向服务器请求房间列表，在服务器的创建房间函数中，向每个终端发送TYPE_COMMAND_ROOM_LIST事件
    //websocket.send(JSON.stringify({command: TYPE_COMMAND_ROOM_LIST}));
  };

  document.getElementById("sendMessage").onclick = () => {
    let textMessage = document.getElementById("textMessage").value;
    websocket.send(JSON.stringify({
      command: TYPE_COMMAND_DIALOGUE,
      userId: userId,
      roomId: roomId,
      message: textMessage
    }));
  };

  document.getElementById("clearLog").onclick = () => {
    let logContentDiv = document.getElementById("logContent");
    while (logContentDiv.hasChildNodes()) {
      logContentDiv.removeChild(logContentDiv.firstChild);
    }
  };

};

const handleMessage = (event) => {
  console.log(event);
  log(event.data);
  let message = JSON.parse(event.data);
  switch (message.command) {
    case TYPE_COMMAND_ROOM_ENTER:
      if (message.message === "joined") {
        log("加入房间：" + message.roomId + "成功");
        roomId = message.roomId;
        openLocalMedia()
        .then(() => {
          log("打开本地音视频设备成功");
          websocket.send(JSON.stringify(
              {command: TYPE_COMMAND_READY, userId: userId, roomId: roomId}));
        })
        .catch(() => {
          log("打开本地音视频设备失败");
        })
      } else {
        log("创建房间：" + message.roomId + "成功");
        caller = true;
        openLocalMedia()
        .then(() => log("打开本地音视频设备成功"))
        .catch(() => log("打开本地音视频设备失败"));
      }

      break;
    case TYPE_COMMAND_ROOM_LIST:
      let roomList = document.getElementById("roomList");
      //这个方法会少删子节点，不知为何，用另一个方法
      /*roomList.childNodes.forEach((node) =>{
          roomList.removeChild(node);
      });*/
      //当div下还存在子节点时 循环继续
      while (roomList.hasChildNodes()) {
        roomList.removeChild(roomList.firstChild);
      }
      JSON.parse(message.message).forEach((roomId) => {
        //大厅默认已经加入，不把大厅展示在房间列表
        if (roomId !== "lobby") {
          let item = document.createElement("div");
          let label = document.createElement("label");
          label.setAttribute("for", roomId);
          label.innerText = "房间号：";
          let span = document.createElement("span");
          span.innerText = roomId;
          let button = document.createElement("button");
          button.innerText = "加入房间";
          button.onclick = () => websocket.send(JSON.stringify({
            command: TYPE_COMMAND_ROOM_ENTER,
            userId: userId,
            roomId: roomId
          }));
          item.append(label, span, button);
          roomList.append(item);
        }
      });
      break;
    case TYPE_COMMAND_DIALOGUE:
      let dialogue = document.createElement("p").innerText = message.userId
          + ":" + message.message;
      let br = document.createElement("br");
      document.getElementById("dialogueList").append(dialogue, br);
      break;
    case TYPE_COMMAND_READY:
      if (caller) {
        //初始化一个webrtc端点
        rtcPeerConnection = new RTCPeerConnection(iceServers);
        //添加事件监听函数
        rtcPeerConnection.onicecandidate = onIceCandidate;
        rtcPeerConnection.ontrack = onTrack;

        //addStream相关的方法，已过时
        //rtcPeerConnection.addTrack(localMediaStream);
        for (const track of localMediaStream.getTracks()) {
          rtcPeerConnection.addTrack(track, localMediaStream);
        }
        rtcPeerConnection.createOffer(offerOptions)
        .then(
            setLocalAndOffer
        )
        .catch(() => {
              log("setLocalAndOffer error:")
            }
        );
      }
      break;
    case TYPE_COMMAND_OFFER:
      if (!caller) {
        //初始化一个webrtc端点
        rtcPeerConnection = new RTCPeerConnection(iceServers);
        //添加事件监听函数
        rtcPeerConnection.onicecandidate = onIceCandidate;

        rtcPeerConnection.ontrack = onTrack;

        //rtcPeerConnection.addTrack(localMediaStream);
        for (const track of localMediaStream.getTracks()) {
          rtcPeerConnection.addTrack(track, localMediaStream);
        }
        //原因：后端接口返回的数据换行采用了\r\n方式，使得json文本无法解析带换行符的内容
        //解决方法：将Json字符串中所有的\r\n转成\\r\\n
        let sdpMessage = message.message;
        sdpMessage.replace(/[\r]/g, "\\r").replace(/[\n]/g, "\\n");
        console.log(sdpMessage);
        let sdp = JSON.parse(sdpMessage).sdp;
        rtcPeerConnection.setRemoteDescription(new RTCSessionDescription(sdp))
        .then(
            log("setRemoteDescription 完毕")
        );
        rtcPeerConnection.createAnswer(offerOptions)
        .then(
            setLocalAndAnswer
        )
        .catch(() => {
              log("setLocalAndAnswer error");
            }
        );
      }
      break;
    case TYPE_COMMAND_ANSWER:
      //原因：后端接口返回的数据换行采用了\r\n方式，使得json文本无法解析带换行符的内容
      //解决方法：将Json字符串中所有的\r\n转成\\r\\n
      let sdpMessage = message.message;
      sdpMessage.replace(/[\r]/g, "\\r").replace(/[\n]/g, "\\n");
      console.log(sdpMessage);
      let sdp = JSON.parse(sdpMessage).sdp;
      rtcPeerConnection.setRemoteDescription(new RTCSessionDescription(sdp))
      .then(
          log("setRemoteDescription 完毕")
      );
      break;
    case TYPE_COMMAND_CANDIDATE:
      let candidateMessage = message.message;
      console.log(candidateMessage);
      let candidate = JSON.parse(candidateMessage).candidate;
      let rtcIceCandidate = new RTCIceCandidate({
        sdpMid: candidate.sdpMid,
        sdpMLineIndex: candidate.label,
        candidate: candidate.candidate
      });
      rtcPeerConnection.addIceCandidate(rtcIceCandidate)
      .then(
          log("addIceCandidate 完毕")
      );
      break;
  }
};

//打开本地音视频,用promise这样在打开视频成功后，再进行下一步操作
const openLocalMedia = () => {
  return new Promise((resolve, reject) => {
    // navigator.mediaDevices.getUserMedia(screenConstraints)
    navigator.mediaDevices.getDisplayMedia(screenConstraints)
    .then((stream) => {
      //make stream available to browser console(设置不设置都没问题)
      //window.stream = mediaStream;
      //localVideo.srcObject = mediaStream;
      localMediaStream = stream;
      let localVideo = document.getElementById("localVideo");
      localVideo.srcObject = localMediaStream;
      localVideo.play();
    })
    .then(() => resolve())
    .catch(() => reject());
  });

};

const onTrack = (event) => {
  let remoteMediaStream = event.streams[0];
  let remoteVideo = document.getElementById("remoteVideo");
  remoteVideo.srcObject = remoteMediaStream;
  remoteVideo.play();

};

const onIceCandidate = (event) => {
  if (event.candidate) {
    log("sending ice candidate");
    websocket.send(JSON.stringify({
      command: TYPE_COMMAND_CANDIDATE,
      userId: userId,
      roomId: roomId,
      message: {
        candidate: {
          sdpMid: event.candidate.sdpMid,
          sdpMLineIndex: event.candidate.sdpMLineIndex,
          candidate: event.candidate.candidate
        }
      }
    }));

  }
};

const setLocalAndOffer = (sessionDescription) => {
  rtcPeerConnection.setLocalDescription(sessionDescription)
  .then(
      log("setLocalDescription 完毕")
  );
  websocket.send(
      JSON.stringify({
        command: TYPE_COMMAND_OFFER,
        userId: userId,
        roomId: roomId,
        message: {
          sdp: sessionDescription,
        }
      })
  );
};

const setLocalAndAnswer = (sessionDescription) => {
  rtcPeerConnection.setLocalDescription(sessionDescription)
  .then(
      log("setLocalDescription 完毕")
  );
  websocket.send(
      JSON.stringify({
        command: TYPE_COMMAND_ANSWER,
        userId: userId,
        roomId: roomId,
        message: {
          sdp: sessionDescription,
        }
      })
  );
};

