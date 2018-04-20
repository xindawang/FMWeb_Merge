$("#device_table").bootstrapTable({ // 对应table标签的id
    url: "/selectAllUserLocation", // 获取表格数据的url
    cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
    striped: true,  //表格显示条纹，默认为false
    // pagination: true, // 在表格底部显示分页组件，默认false
    pageList: [10, 20], // 设置页面可以显示的数据条数
    pageSize: 10, // 页面数据条数
    pageNumber: 1, // 首页页码
    search:true,//搜索框
    searchOnEnterKey:true,//设置为 true时，按回车触发搜索方法，否则自动触发搜索方法
    showRefresh:true,//刷新按钮
    // showHeader:true,//列头
    // showFooter:true,//列尾
    showColumns:true,
    // smartDisplay:true,//设置为 true可以在分页和卡片视图快速切换
    showToggle:true,//是否显示 切换试图（table/card）按钮
    showPaginationSwitch:true,//数据条选择框
    searchAlign:'left',
    buttonsAlign:'left',
    columns: [
        {
            field: 'device', // 返回json数据中的name
            title: '设备', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            field: 'x',
            title: 'X',
            align: 'center',
            valign: 'middle'
        },
        {
            field: 'y', // 返回json数据中的name
            title: 'Y', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            field: 'saveTime', // 返回json数据中的name
            title: '记录时间', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            field: 'no', // 返回json数据中的name
            title: '编号', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            title: "操作",
            align: 'center',
            valign: 'middle',
            width: 160, // 定义列的宽度，单位为像素px
            formatter: function (value, row, index) {
                return '<button class="btn btn-danger btn-sm" onclick="del('+row.no+')">删除</button>';
            }
        }
    ],
    onLoadSuccess: function(){  //加载成功时执行
        console.info("加载成功");
    },
    onLoadError: function(){  //加载失败时执行
        console.info("加载数据失败");
    }

});

function del(a) {
  alert(a);
}