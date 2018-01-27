var planDataTable = $("#myTable").table(
    {
        url : "/getData",
        type:"get",
        columns : [
            {
                title: '设备',
                data: 'device'
            },
            {
                title: 'X',
                data: 'x'
            },
            {
                title: 'Y',
                data: 'y'
            },
            {
                title: '记录时间',
                data: 'saveTime'
            },
            {
                title: '编号',
                data: 'no'
            },

            {
                title : '操作',
                element : "<button  class='btn btn-danger delete'>删除</button>"
            } ],
        afterDraw : function() {

        }
    });