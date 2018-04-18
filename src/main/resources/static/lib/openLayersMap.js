//地图设置中心
var center = ol.proj.transform([100, 50], 'EPSG:4326', 'EPSG:3857');
// 计算图片映射到地图上的范围，图片像素为 1159*600，保持比例的情况下，把分辨率放大一些
var img_extent = [center[0]- 1159*1200/2, center[1]-600*1200/2, center[0]+1159*1200/2, center[1]+600*1200/2];
//限制view的拖动范围
var view_extent = [center[0]- 1159*500/2, center[1]-600*500/2, center[0]+1159*500/2, center[1]+600*500/2];
var users = new Array();
var userLocation;
//获取所有用户信息
GetAllInfo();
//创建地图
var map = new ol.Map({
    controls: ol.control.defaults().extend([
        new ol.control.FullScreen(),
        new ol.control.ZoomSlider(),
    ]),
    view: new ol.View({
        center: center,
        zoom: 7,
        minZoom: 6,
        maxZoom: 8,
        extent:view_extent,
    }),
    target: 'map',
});

// 加载静态地图层
map.addLayer(new ol.layer.Image({
    source: new ol.source.ImageStatic({
        url: '/static/img/iotlab.jpg', // iot地图
        imageExtent: img_extent,     // 映射到地图的范围
    })
}));


// 创建一个用于放置活动图标的layer
var activityLayer = new ol.layer.Vector({
    source: new ol.source.Vector()
});
//标示出每个用户
for (var i=0;i<users.length;i++){
    var user_x = users[i]["x"];
    var user_y = users[i]["y"];
    var no = users[i]["no"];
    var flag = new ol.Feature({
        geometry: new ol.geom.Point([user_x,user_y])
    });
// 设置Feature的样式，使用用户图标
    flag.setStyle(new ol.style.Style({
        image: new ol.style.Icon({
            src: 'static/img/'+no+'.png',
            anchor: [0.5, 1],
            scale: 0.5
        })
    }));
    flag.setId(no);
    activityLayer.getSource().addFeature(flag);
}
/**
 *添加文字标注
 */
var text1 = new ol.Feature({
    geometry: new ol.geom.Point(ol.proj.transform([98.95, 49.563], 'EPSG:4326', 'EPSG:3857'))
});
text1.setStyle(new ol.style.Style({
    text: new ol.style.Text({
        font: 'bold 14px 微软雅黑',//默认这个字体，可以修改成其他的，格式和css的字体设置一样
        text: '石老师办公室',
        fill: new ol.style.Fill({
            color: 'black'
        })
    })
}));
activityLayer.getSource().addFeature(text1);
var text2 = new ol.Feature({
    geometry: new ol.geom.Point(ol.proj.transform([97.765, 49.563], 'EPSG:4326', 'EPSG:3857'))
});
text2.setStyle(new ol.style.Style({
    text: new ol.style.Text({
        font: 'bold 14px 微软雅黑',//默认这个字体，可以修改成其他的，格式和css的字体设置一样
        text: '会议室',
        fill: new ol.style.Fill({
            color: 'black'
        })
    })
}));
activityLayer.getSource().addFeature(text2);
var text3 = new ol.Feature({
    geometry: new ol.geom.Point(ol.proj.transform([98.666, 51.15], 'EPSG:4326', 'EPSG:3857'))
});
text3.setStyle(new ol.style.Style({
    text: new ol.style.Text({
        font: 'bold 14px 微软雅黑',//默认这个字体，可以修改成其他的，格式和css的字体设置一样
        text: '无线网络实验室',
        fill: new ol.style.Fill({
            color: 'black'
        })
    })
}));
activityLayer.getSource().addFeature(text3);
var text4 = new ol.Feature({
    geometry: new ol.geom.Point(ol.proj.transform([101.817, 51.15], 'EPSG:4326', 'EPSG:3857'))
});
text4.setStyle(new ol.style.Style({
    text: new ol.style.Text({
        font: 'bold 14px 微软雅黑',//默认这个字体，可以修改成其他的，格式和css的字体设置一样
        text: '徐老师办公室',
        fill: new ol.style.Fill({
            color: 'black'
        })
    })
}));
activityLayer.getSource().addFeature(text4);
var text5 = new ol.Feature({
    geometry: new ol.geom.Point(ol.proj.transform([103.317, 51.15], 'EPSG:4326', 'EPSG:3857'))
});
text5.setStyle(new ol.style.Style({
    text: new ol.style.Text({
        font: 'bold 14px 微软雅黑',//默认这个字体，可以修改成其他的，格式和css的字体设置一样
        text: '大数据实验室',
        fill: new ol.style.Fill({
            color: 'black'
        })
    })
}));
activityLayer.getSource().addFeature(text5);
map.addLayer(activityLayer);

/**
*点击事件
 */
map.on('click', function(event){
    var coordinate = event.coordinate;
    // var msg = ol.proj.transform(coordinate, 'EPSG:3857', 'EPSG:4326');
    // alert("X:"+msg.toString().split(",")[0]+" Y:"+msg.toString().split(",")[1]);
    //对每个feature判断是否点中
    var feature = map.forEachFeatureAtPixel(event.pixel, function(feature) {
        return feature;
    });
    if(feature){
        var id=feature.getId();
        if(id!=null){
            var overlay=createPopup(id.toString(),coordinate);
            map.addOverlay(overlay)
        }
    }
});


/**
 *创建气泡
 */
function createPopup(id,coordinate) {
    var map=document.getElementById("map");
    var div=document.getElementById("popup"+id);
    if(div==null){
        div=document.createElement("div");
        div.setAttribute("id","popup"+id);
        div.setAttribute("class","ol-popup");
    }
    var a=document.getElementById("popup-closer"+id);
    if(a==null){
        a=document.createElement("a");
        a.setAttribute("id","popup-closer"+id);
        a.setAttribute("href","#");
        a.setAttribute("class","ol-popup-closer");
    }
    var div1=document.getElementById("text"+id);
    if(div1==null){
        div1=document.createElement("div");
        div1.setAttribute("id","text"+id);
    }
    div.appendChild(a);
    div.appendChild(div1);
    map.appendChild(div);

    a.onclick = function() {
        overlay.setPosition(undefined);
        a.blur();
        return false;
    };
    var overlay = new ol.Overlay(({
        element: div,
        autoPan: true,
        autoPanAnimation: {
            duration: 250   //当Popup超出地图边界时，为了Popup全部可见，地图移动的速度.
        }
    }));
    GetUserInfo(id);
    var user_x =userLocation["x"];
    var user_y =userLocation["y"];
    var saveTime=userLocation["saveTime"];
    var device=userLocation["device"];
    div1.innerHTML = '<p>坐标：</p><code>' + user_x+','+user_y + '</code><br/>'+
    '用户：<code>'+device+'</code><br/>'+
    '保存时间：<code>'+saveTime+'</code>';
    overlay.setPosition(coordinate);
    return overlay;

}

//ajax请求获得所有用户信息
function GetAllInfo() {
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
//ajax请求获得单个用户信息
function GetUserInfo(id) {
    $.ajax({
        type: "post",
        url: "/selectUserLocation/"+id,
        data: {},
        contentType: "application/x-www-form-urlencoded",
        cache: false,
        async:false,
        dataType: "json",
        success: function (data) {
            var json = 'window.loc = ' + JSON.stringify(data);
            Object.prototype.toString.constructor(json)();
            userLocation=loc;
        }
    });
}