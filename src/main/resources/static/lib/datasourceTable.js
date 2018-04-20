
$("#datasource_table").bootstrapTable({ // 对应table标签的id
    url: "/getDatasourceInfo", // 获取表格数据的url
    cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
    striped: true,  //表格显示条纹，默认为false
    pagination: true, // 在表格底部显示分页组件，默认false
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
            field: 'id', // 返回json数据中的name
            title: 'id', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            field: 'user_id',
            title: '用户id',
            align: 'center',
            valign: 'middle'
        },
        {
            field: 'device_id', // 返回json数据中的name
            title: '设备id', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            field: 'data_path', // 返回json数据中的name
            title: '数据路径', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            field: 'map_path', // 返回json数据中的name
            title: 'AP关系文件', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            field: 'time', // 返回json数据中的name
            title: '采样次数', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            field: 'upload_time', // 返回json数据中的name
            title: '上传时间', // 表格表头显示文字
            align: 'center', // 左右居中
            valign: 'middle' // 上下居中
        },
        {
            title: "操作",
            align: 'center',
            valign: 'middle',
            width: 160, // 定义列的宽度，单位为像素px
            formatter: function (value, row, index) {
                return '<button class="btn btn-danger btn-sm" onclick="">删除</button>'+
                '<a class="btn btn-primary btn-sm" onclick="return conf('+row.id+')"  >生成指纹</a>';
            }


        }
    ],
    onLoadSuccess: function(){  //加载成功时执行
        console.info("加载成功");
    },
    onLoadError: function(){  //加载失败时执行
        console.info("加载数据失败");
    }

})

function conf(id) {
  if(confirm("确定生成指纹吗？")){
      $.ajax({
          type: "post",
          url: "/tableExist",
          data: {'id':id},
          cache: false,
          async: false,
          dataType: "json",
          success: function (data) {
              if(data){
                  alert("指纹已存在");
              }else {
                  generate(id);
              }
          }
      });
  }else {
      return false;
  }
}

function generate(id) {
    $.ajax({
        type: "post",
        url: "/generate/"+id,
        data: {},
        cache: false,
        async: false,
        dataType: "json",
        success: function (data) {
            alert("成功生成");
        }
    });
}