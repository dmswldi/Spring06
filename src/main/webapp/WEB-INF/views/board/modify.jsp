<%--
  Created by IntelliJ IDEA.
  User: eunjikim
  Date: 2021/05/09
  Time: 9:01 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
<%@include file="../includes/footer.jsp"%>

<script type="text/javascript">
$(document).ready(function(){
  var formObj = $("form");

  $('button').on("click", function(e){
     e.preventDefault();

     var operation = $(this).data("oper"); //dataset.oper; (X)
     console.log('operation? : '+ operation);

     /* Remove */
     if(operation === 'remove'){// 얘도 empty()하고 bno만 보내도 될텐데...
       formObj.attr("action", "/board/remove");
     /* forwarding to List */
    } else if(operation === 'list'){
       //self.location = "/board/list";
       //return;
       formObj.attr("action", "/board/list").attr("method", "get");
       var pageNumTag = $('input[name="pageNum"]').clone();// 미리 복제해놓고
       var amountTag = $('input[name="amount"]').clone();
       var typeTag = $('input[name="type"]').clone();
       var keywordTag = $('input[name="keyword"]').clone();

       formObj.empty();// 자식들 remove -> 파라미터 없애기
       formObj.append(pageNumTag);
       formObj.append(amountTag);
       formObj.append(typeTag);
       formObj.append(keywordTag);
       // appendTo(): target의 마지막에 element 넣기 (~에 첨부하다), old location 지워짐
       // -> clone(): element deep copy 사용

       // remove(): 본인 포함 자식 모두 삭제, 이벤트 삭제
       // detach(): 본인 포함 자식 모두 삭제, 이벤트 유지(데이터 복구 가능)
       // empty(): 자식 모두 삭제
       // upwrap(): 부모1 삭제
    }
     formObj.submit();
  });

});
</script>