
//get 请求
const get = (url,params) => {
    if (params) {

        let paramsArray = [];
        //拼接参数
        Object.keys(params).forEach(key =>
            paramsArray.push(key + '=' + encodeURI(params[key].toString())));

        if (paramsArray.length > 0) {
            if (url.search(/\?/) === -1) {
                url += '?' + paramsArray.join('&');
            } else {
                url += '&' + paramsArray.join('&');
            }
        }
    }
    return new Promise((resolve, reject) => {
        fetch(url)
            .then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err));
    });

};
//post 请求
const post = (url, data) => {
    return new Promise((resolve, reject) => {
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json',
            },
            body: JSON.stringify(data),
        })
            .then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err));
    });
};

//put 请求
const restPut = (url, data)=> {
    return new Promise((resolve, reject) => {
        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))

    });
};
//delete 请求
const restDelete=(url, data)=> {
    return new Promise((resolve, reject) => {
        fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(res => res.json())
            .then(data => resolve(data))
            .catch(err => reject(err))
    });
};


const namePool = [
    "去", "年", "元", "夜", "时",
    "花", "市", "灯", "如", "昼",
    "月", "上", "柳", "梢", "头",
    "人", "约", "黄", "昏", "后",
    "今", "年", "元", "夜", "时",
    "花", "与", "灯", "依", "旧",
    "不", "见", "去", "年", "人",
    "泪", "满", "春", "衫", "袖"
];
//生成随机名字
const randomName = () => {

    let randomName = "";
    for (let i = 0; i < 3; i++) {
        //获取随机数
        let rand = Math.floor(Math.random() * namePool.length);
        // 随机从数组中取出某值
        let randomElement = namePool[rand];
        randomName += randomElement;
    }
    return randomName;
};
