var planDataTable = $("#datasource_table").table(
    {
        url : "/getDatasourceInfo",
        type:"get",
        columns : [
            {
                title: 'id',
                data: 'id'
            },
            {
                title: '用户id',
                data: 'user_id'
            },
            {
                title: '设备id',
                data: 'device_id'
            },
            {
                title: '数据路径',
                data: 'data_path'
            },
            {
                title: 'AP关系文件',
                data: 'map_path'
            },
            {
                title: '采样次数',
                data: 'time'
            },
            {
                title: '上传时间',
                data: 'upload_time'
            },
            {
                title: '备注',
                data: 'remarks'
            },
            {
                title : '操作',
                element : "<button  class='btn btn-danger delete'>删除</button>"
            } ],
        afterDraw : function() {

        }
    });