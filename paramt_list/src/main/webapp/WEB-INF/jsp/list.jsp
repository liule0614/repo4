<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: lc
  Date: 2019/12/4
  Time: 14:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
    $(function () {

        $("[value='全选']").click(function () {
            $(".datatable").attr("checked",true);
        })
        $("[value='全不选']").click(function () {
            $(".datatable").attr("checked",false);
        })
        $("[value='反选']").click(function () {
            if ($(".datatable").attr("checked")){
                $(".datatable").attr("checked",false);
            }else {
                $(".datatable").attr("checked",true);
            }

        })

        $("#qx").click(function () {
            $(".tablefield").attr("checked",true);
        })
        $("#qbx").click(function () {
            $(".tablefield").attr("checked",false);
        })
        $("#fx").click(function () {
            if ($(".tablefield").attr("checked")){
                $(".tablefield").attr("checked",false);
            }else {
                $(".tablefield").attr("checked",true);
            }
        })

        $("#ip").change(function () {
            var id=$(this).val();
            $(".opt").remove();
            $.post(
                "showDataBase",
                {id:id},
                function (data) {
                    for (var i = 0;i<data.length;i++){
                        $("#sourceDateBase").append("<option value='"+data[i].Database+"' class='opt'>"+data[i].Database+"</option>")
                    }
                },"json"
            );
        })
        $("#sourceDateBase").change(function () {
            $("tr").remove();
            var dataName=$(this).val();
            $.post(
                "queryTableName",
                {dataName:dataName},
                function (data) {
                    var table = "";
                    for (var i = 0;i<data.length;i++){
                        table = "<td><input type='checkbox' value='"+data[i].table+"' class='datatable'></td><td>"+data[i].table+"</td><td><input type='button' value='迁移' onclick='removeval(&#39;"+ data[i].table + " &#39;)'></td><td><input type='button' value='选择字段迁移' onclick='removedel(&#39;"+ data[i].table + " &#39;)'></td>";
                        $("#dataBaseTable").append("<tr>"+table+"</tr>");
                    }
                },"json"
            );
        });
    })
    //展示所选表的字段
    function removedel(tablename) {
        $("tr").remove();
        $("#tableField").append("<tr><td colspan='2'>"+tablename+"</td></tr>");
        $.post(
            "queryTableField",
            {tablename:tablename},
            function (data) {
                var table = "";
                for (var i = 0;i<data.length;i++){
                    table = "<td><input type='checkbox' value='"+data[i]+"' class='tablefield'></td><td>"+data[i]+"</td>";
                    $("#tableField").append("<tr>"+table+"</tr>");
                }
                $("#tableField").append("<tr><td colspan='2'><input type='button' value='迁移' id='btn'></td></tr>")
            },"json"
        );
    }
    //整表迁移
    function removeval(tablename){
        $("tr").remove();
        $("#targetBase").append("<tr><td colspan='2'>"+tablename+"</td></tr>");

    }


</script>
<html>
<head>
    <title>Title</title>
</head>
<body>

    选择源数据库
    <select id="ip">
        <option value="0" selected="selected">请选择</option>
        <c:forEach items="${list}" var="list">
            <option value="${list.id}">${list.name}</option>
        </c:forEach>
    </select>
    <br>
    请选择数据库
    <select id="sourceDateBase">
         <option>-----请选择-----</option>
     </select>
    <br>
    请选择表
    <table id="dataBaseTable" border="1">
        <br>
        <input type="button" value="全选">
        <input type="button" value="全不选">
        <input type="button" value="反选">
        <input type="button" value="批量迁移">
    </table>
    请选择字段
    <table id="tableField" border="1">
        <br>
        <input type="button" value="全选" id="qx">
        <input type="button" value="全不选" id="qbx">
        <input type="button" value="反选" id="fx">
    </table>

    <table id="targetBase" border="1">
        <br>
        选择目标数据库：
    </table>
    <select id="targetip">
        <option value="0" selected="selected">-----请选择-----</option>
    </select>

</body>
</html>
