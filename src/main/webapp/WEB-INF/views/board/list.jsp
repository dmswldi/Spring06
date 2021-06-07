<%--
  Created by IntelliJ IDEA.
  User: eunjikim
  Date: 2021/05/08
  Time: 12:13 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="../includes/header.jsp"%>

                <!-- Page Heading -->
                <h1 class="h3 mb-2 text-gray-800">Board Tables</h1>
                <p class="mb-4">DataTables is a third party plugin that is used to generate the demo table below.
                    For more information about DataTables, please visit the <a target="_blank"
                                                                               href="https://datatables.net">official DataTables documentation</a>.</p>

                <!-- DataTales Example -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Board List Page
                            <button id="regBtn" type="button" class="btn btn-xs border float-right">Register New Board</button>
                        </h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>#번호</th>
                                    <th>제목</th>
                                    <th>작성자</th>
                                    <th>작성일</th>
                                    <th>수정일</th>
                                </tr>
                                </thead>

                                <c:forEach items="${list}" var="board">
                                    <tr>
                                        <td><c:out value="${board.bno}" /></td>
                                        <td><a class="move" href="<c:out value='${board.bno}'/>">
                                            <c:out value="${board.title}"/>
                                            <b> [<c:out value="${board.replyCnt}"/>]</b>
                                        </a></td>
                                        <td><c:out value="${board.writer}" /></td>
                                        <td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.regdate}" /></td>
                                        <td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.updateDate}" /></td>
                                    </tr>
                                </c:forEach>
                            </table>

                            <!-- Search Form -->
                            <div class="row">
                                <div class="col-lg-10">
                                    <form id="searchForm" action="/board/list" method="get" class="row justify-content-center">
                                        <div class="col-lg-3">
                                            <select name="type" class="custom-select">
                                                <option value=""
                                                    <c:out value="${pageMaker.cri.type eq null? 'selected':''}"/>>--</option>
                                                <option value="T"
                                                    <c:out value="${pageMaker.cri.type eq 'T'? 'selected':''}"/>>제목</option>
                                                <option value="C"
                                                    <c:out value="${pageMaker.cri.type eq 'C'? 'selected':''}"/>>내용</option>
                                                <option value="W"
                                                    <c:out value="${pageMaker.cri.type eq 'W'? 'selected':''}"/>>작성자</option>
                                                <option value="TC"
                                                    <c:out value="${pageMaker.cri.type eq 'TC'? 'selected':''}"/>>제목 or 내용</option>
                                                <option value="TW"
                                                    <c:out value="${pageMaker.cri.type eq 'TW'? 'selected':''}"/>>제목 or 작성자</option>
                                                <option value="TWC"
                                                    <c:out value="${pageMaker.cri.type eq 'TWC'? 'selected':''}"/>>제목 or 내용 or 작성자</option>
                                            </select>
                                        </div>
                                        <div class="col-lg-5">
                                            <input type="text" name="keyword" class="form-control" value="<c:out value='${pageMaker.cri.keyword}'/>"/>
                                            <input type="hidden" name="pageNum" value="<c:out value='${pageMaker.cri.pageNum}'/>">
                                            <input type="hidden" name="amount" value="<c:out value='${pageMaker.cri.amount}'/>">
                                        </div>
                                        <div class="col-lg-1">
                                            <button class="btn btn-default">Search</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <!-- end Search Form -->

                            <!-- Pagination -->
                            <div class="float-right">
                                <ul class="pagination">
                                    <li class="page-item ${pageMaker.prev == true? '':'disabled'}">
                                        <a class="page-link" href="${pageMaker.startPage-1}">Previous</a>
                                    </li>

                                    <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
                                        <li class="page-item ${pageMaker.cri.pageNum == num? 'active':''}">
                                            <a class="page-link" href="${num}">${num}</a>
                                        </li>
                                    </c:forEach>

                                    <li class="page-item ${pageMaker.next == true? '':'disabled'}">
                                        <a class="page-link" href="${pageMaker.endPage + 1}">Next</a>
                                    </li>
                                </ul>
                            </div>
                            <!-- end Pagination -->

                            <!-- Paging Form -->
                            <form id="actionForm" action="/board/list" method="get">
                                <input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
                                <input type="hidden" name="amount" value="${pageMaker.cri.amount}">
                                <input type="hidden" name="type" value="<c:out value='${pageMaker.cri.type}'/>">
                                <input type="hidden" name="keyword" value="<c:out value='${pageMaker.cri.keyword}'/>">
                            </form>
                            <!-- end Paging Form -->

                            <!-- Modal -->
                            <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
                                aria-labelledby="myModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"
                                                    aria-hidden="true">&times;</button>
                                            <h4 class="modal-title" id="myModalLabel">Modal title</h4>
                                        </div>
                                        <div class="modal-body">처리가 완료되었습니다.</div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                            <button type="button" class="btn btn-primary">Save changes</button>
                                        </div>
                                    </div>
                                    <!-- /.modal-content -->
                                </div>
                                <!-- /.modal-dialog -->
                            </div>
                            <!-- /.modal -->

                        </div>
                    </div>
                </div>

<%@include file="../includes/footer.jsp"%>

<script type="text/javascript">
    $(document).ready(function(){
       var result = '<c:out value="${result}"/>';

       checkModal(result);

       /* stack 구조로 동작하는 window.history 객체 for 브라우저의 세션 기록 조작 */
       history.replaceState({}, null, null);

       function checkModal(result){
           if (result == '' || history.state) {
               return;
           }

           if (parseInt(result) > 0) {
               $(".modal-body").html("게시글 " + parseInt(result) + "번이 등록되었습니다.")
           }

           $("#myModal").modal("show");
       }

       $("#regBtn").on("click", function(){
           self.location = "/board/register";
       });


       /* Pagination */
       var actionForm = $('#actionForm');
       $('.page-item a').on("click", function(e){
          e.preventDefault();
          console.log("click");

          // page 값 변경
          actionForm.find("input[name='pageNum']").val($(this).attr("href"));
          actionForm.submit();
       });

       $('.move').on('click', function(e){
           e.preventDefault();

           actionForm.append('<input type="hidden" name="bno" value="' + $(this).attr('href') + '"/>');
           actionForm.attr('action', '/board/get');
           actionForm.submit();
       });

        /* Search */
        var searchForm = $('#searchForm');

        $('#searchForm button').on('click', function(e){
           if(!searchForm.find('option:selected').val()){// 사용법
               alert("검색 종류를 선택하세요")
               return false;
           }

            if(!searchForm.find('input[name="keyword"]').val()){
                alert("키워드를 입력하세요");
                return false;
            }

            searchForm.find('input[name="pageNum"]').val("1");// val(a String)
            e.preventDefault();

            searchForm.submit();
        });

    });
</script>