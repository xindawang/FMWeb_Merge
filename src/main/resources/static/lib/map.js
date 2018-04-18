//获取版本号,设置title
document.title = '基础地图显示V' + fengmap.VERSION;


//定义全局map变量
var map;
var fmapID = 'iotlab';
//用户对象数组
var users = new Array();
//打开网页立即请求一次信息，因为请求需要时间，请求后瞬间标位置是无效的
ajaxGetInfo();
window.onload = function () {
    map = new fengmap.FMMap({
        //渲染dom
        container: document.getElementById('fengMap'),
        //地图数据位置
        mapServerURL: 'static/data/' + fmapID,
        //主题数据位置
        mapThemeURL: 'static/data/theme',
        //设置主题
        defaultThemeName: 'iotlab',
        // [200, 4000]， 自定义比例尺范围，单位（厘米）
        mapScaleRange: [1, 4000],
        // 默认自定义比例尺为 1：1000（厘米）
        defaultMapScale: 110,
        //开发者申请应用下web服务的key
        key: '34a37f702ce7100a9043b5ef4d583500',
        //开发者申请应用名称
        appName: '蜂鸟室内地图测试',
        //起始2d/3d
        defaultViewMode: fengmap.FMViewMode.MODE_2D
    });


    //打开Fengmap服务器的地图数据和主题
    map.openMapById(fmapID, function (error) {
        //打印错误信息
        console.log(error);
    });
    // 2D、3D控件配置
    var toolControl = new fengmap.toolControl(map, {
        init2D: true,   //初始化2D模式
        groupsButtonNeeded: false,   //设置为false表示只显示2D,3D切换按钮
        //点击按钮的回调方法,返回type表示按钮类型,value表示对应的功能值
        clickCallBack: function (type, value) {
            // console.log(type,value);
        },

        position: fengmap.controlPositon.RIGHT_TOP, //位置 左上角
        //位置x,y的偏移量
        offset: {
            x: 10,
            y: 0
        }
    });
    var draw = false;
    //地图点击事件
    map.on('mapClickNode', function (event) {
        if (!draw) {
            var groupLayer = map.getFMGroup(1);
            var layer = map.getLayerByAlias(1, "imageMarker", function () {
            });
            if (layer) {
                groupLayer.removeLayer(layer);
            }
            drawUserImage();
            draw = true;
        }
        //点到东西了，显示信息
        switch (event.nodeType) {
            case fengmap.FMNodeType.MODEL:
                //返回模型信息
                break;
            case fengmap.FMNodeType.TEXT_MARKER:
                //返回用户自定义标注信息
                break;
            case fengmap.FMNodeType.IMAGE_MARKER:
                //返回用户自定义图片标注
                var info_x = "?";
                var info_y = "?";
                var info_device = "?";
                var info_saveTime = "?";
                for (var i = 0; i < users.length; i++) {
                    var user_x = users[i]["x"];
                    var user_y = users[i]["y"];
                    var saveTime = new Date().Format(users[i]["saveTime"]);
                    var device = users[i]["device"];
                    if (Math.abs(user_x - event.target.x) < 1 && Math.abs(user_y - event.target.y) < 1) {
                        info_device = device;
                        info_x = user_x;
                        info_y = user_y;
                        info_saveTime = saveTime;
                        break;
                    }

                }
                var ctlOpt = new fengmap.controlOptions({
                    mapCoord: {
                        //设置弹框的x轴
                        x: event.target.x,
                        //设置弹框的y轴
                        y: event.target.y,
                        z: 1,
                        //设置弹框位于的楼层
                        groupID: 1
                    },
                    //设置弹框的宽度
                    width: 220,
                    //设置弹框的高度
                    height: 150,
                    marginTop: 1,

                    //设置弹框的内容
                    content: '<span style="font-size:7px">设备:' + info_device + '</span><br>' +
                    '<span style="font-size:7px">X:' + info_x + '</span><br>' +
                    '<span style="font-size:7px">Y:' + info_y + '</span><br>' +
                    '<span style="font-size:7px">保存时间:' + info_saveTime + '</span>'
                });
                var popMarker = new fengmap.FMPopInfoWindow(map, ctlOpt);
                break;
        }
    });
};

function drawUserImage() {
    var groupLayer = map.getFMGroup(1);
    var layer = new fengmap.FMImageMarkerLayer("imageMarker");
    groupLayer.addLayer(layer);
    //截止日期，之前不显示
    var EndTime = new Date().Format("1998-2-13 12:22:12");
    for (var i = 0; i < users.length; i++) {
        var user_x = users[i]["x"];
        var user_y = users[i]["y"];
        var saveTime = new Date().Format(users[i]["saveTime"]);
        var no = users[i]["no"];
        if (saveTime > EndTime) {
            var im = new fengmap.FMImageMarker({
                x: user_x,
                y: user_y,
                z: 1,
                url: 'static/img/' + no + '.png', //设置图片路径
                size: 30,                               //设置图片显示尺寸
                //图片标注渲染完成的回调方法
                callback: function () {
                    // 在图片载入完成后，设置 "一直可见",即显示优先级最高
                    // 如相同位置有其他标注，则此标注在前。
                    im.alwaysShow();
                }
            });

            layer.addMarker(im);
        }
        else {
            continue;
        }
    }
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//ajax请求获得用户信息
function ajaxGetInfo() {
    $.ajax({
        type: "post",
        url: "/selectAllUserLocation",
        data: {},
        contentType: "application/x-www-form-urlencoded",
        cache: false,
        async:false,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            //遍历data 把每个用户分离出来加入users
            for (var i = 0; i < data.length; i++) {
                //直接var接受JSON是获取不到值的，需要挂载再window全局下面
                var json = 'window.user = ' + JSON.stringify(data[i]);
                Object.prototype.toString.constructor(json)();
                users[i] = user;
            }
        }
    });
}

//刷新地图
$("#button").click(function () {
    ajaxGetInfo();
    var groupLayer = map.getFMGroup(1);
    var layer = map.getLayerByAlias(1, "imageMarker", function () {
    });
    if (layer) {
        groupLayer.removeLayer(layer);
    }
    drawUserImage();
});