<%--
  Created by IntelliJ IDEA.
  User: eunjikim
  Date: 2021/05/09
  Time: 9:01 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
var bno = '<c:out value="${board.bno}"/>';
</script>
<script type="text/javascript" src="/resources/js/board/modify.js"></script>
<link type="text/css" rel="stylesheet" href="/resources/css/board/get.css">

<%@include file="../includes/header.jsp"%>

<div class="row">
  <div class="col-lg-12">
    <h1 class="page-header">Board Modify</h1>
  </div>
</div>
<!-- /.col-lg-12 -->

<div class="row">
  <div class="col-lg-12">
    <div class="card">

      <div class="card-header">Board Modify Page</div>
      <!-- /.panel-heading -->
      <div class="card-body">

        <form role="form" action="/board/modify" method="post">
          <input type="hidden" name="pageNum" value="<c:out value='${cri.pageNum}'/>">
          <input type="hidden" name="amount" value="<c:out value='${cri.amount}'/>">
          <input type="hidden" name="type" value="<c:out value='${cri.type}'/>">
          <input type="hidden" name="keyword" value="<c:out value='${cri.keyword}'/>">

          <div class="form-group">
            <label>Bno</label>
            <input class="form-control" name="bno" value='<c:out value="${board.bno}"/>' readonly>
          </div>

          <div class="form-group">
            <label>Title</label>
            <input class="form-control" name="title" value='<c:out value="${board.title}"/>'>
          </div>

          <div class="form-group">
            <label>Text area</label>
            <textarea class="form-control" rows="3" name="content"><c:out value="${board.content}"/></textarea>
          </div>

          <div class="form-group">
            <label>Writer</label>
            <input class="form-control" name="writer" value='<c:out value="${board.writer}"/>'>
          </div>

          <div class="form-group">
            <label>RegDate</label>
            <input class="form-control" name="regDate"
              value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.regdate}"/>' readonly>
          </div>

          <div class="form-group">
            <label>Update Date</label>
            <input class="form-control" name="updateDate"
              value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.updateDate}"/>' readonly>
          </div>

          <button type="submit" data-oper="modify" class="btn btn-default">Modify</button>
          <button type="submit" data-oper="remove" class="btn btn-danger">Remove</button>
          <button type="submit" data-oper="list" class="btn btn-info">List</button>
        </form>

      </div>
      <!-- end panel-body -->

    </div>
    <!-- end panel -->
  </div>

</div>
<!-- /.row -->


<!-- Attached File -->
<div class="row">
  <div class="col-lg-12">
    <div class="card">
      <div class="card-header">Files</div>

      <div class="card-body">
        <div class="form-group uploadDiv">
          <input type="file" name="uploadFile" multiple>
        </div>

        <div class="uploadResult">
          <ul>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="../includes/footer.jsp"%>