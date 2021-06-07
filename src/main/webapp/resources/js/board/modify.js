$(document).ready(function(){

    var formObj = $("form");
    /* Modify/Remove btn Click */
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
        } else if(operation === 'modify') {
            console.log('modify clicked');

            var str = '';

            $('.uploadResult ul li').each(function(i, obj){
               var jobj = $(obj);

               console.dir(jobj);

               str += '<input type="hidden" name="attachList[' + i + '].fileName" value="' + jobj.data('filename') + '">'
                    + '<input type="hidden" name="attachList[' + i + '].uuid" value="' + jobj.data('uuid') + '">'
                    + '<input type="hidden" name="attachList[' + i + '].uploadPath" value="' + jobj.data('path') + '">'
                    + '<input type="hidden" name="attachList[' + i + '].fileType" value="' + jobj.data('type') + '">';
            });
            formObj.append(str).submit();
        }
        formObj.submit();
    });


    /* 첨부파일 보여주기, 즉시 실행 함수 */
    (function(){

        $.getJSON('/board/getAttachList', {bno: bno}, function(arr){
           console.log(arr);

           var str = '';

            $(arr).each(function(i, attach){
                if(attach.fileType){// image일 때
                    var fileCallPath = encodeURIComponent(attach.uploadPath + '/s_' + attach.uuid + "_" + attach.fileName);

                    str += '<li data-path="' + attach.uploadPath + '" data-uuid="' + attach.uuid + '" data-filename="' + attach.fileName + '" data-type="' + attach.fileType + '">'
                        + '<div><span>' + attach.fileName + '</span>'
                        + ' <button data-file=\"' + fileCallPath + '\" data-type="image" class="btn btn-warning btn-circle"><i class="fa fa-times"></i></button><br>'
                        + '<img src="/display?fileName=' + fileCallPath + '"></div>'
                        + '</li>';
                } else {// 일반 파일일 때
                    var fileCallPath = encodeURIComponent(attach.uploadPath + '/' + attach.uuid + "_" + attach.fileName);

                    str += '<li data-path="' + attach.uploadPath + '" data-uuid="' + attach.uuid + '" data-filename="' + attach.fileName + '" data-type="' + attach.fileType + '">'
                        + '<div><span>' + attach.fileName + '</span><br>'
                        + ' <button data-file=\"' + fileCallPath + '\" data-type="file" class="btn btn-warning btn-circle"><i class="fa fa-times"></i></button><br>'
                        + '<img src="/resources/img/attach.png">'
                        + '</div>'
                        + '</li>';
                }
            });
            $('.uploadResult ul').html(str);
        });

        /* 첨부파일 x 버튼 클릭 시 */
        $('.uploadResult').on('click', 'button', function(e){
            console.log('delete file');

            if(confirm('Remove this file?')){
                var targetLi = $(this).closest('li');
                targetLi.remove();
            }
        });

    })();



    /* File Check */
    var regex = new RegExp('(.*?)\.(exe|sh|zip|alz)$');
    var maxSize = 5242880;// 5MB

    function checkExtension(fileName, fileSize){
        if(fileSize >= maxSize){
            alert('파일 사이즈 초과');
            return false;
        }

        if(regex.test(fileName)){
            alert('해당 종류의 파일은 업로드할 수 없습니다.');
            return false;
        }
        return true;
    }

    /* Thumbnail Show */
    function showUploadResult(uploadResultArr){// List<AttachFileDTO>
        if(!uploadResultArr || uploadResultArr.length == 0){
            return ;
        }

        var uploadUL = $('.uploadResult ul');
        var str = '';

        $(uploadResultArr).each(function(i, obj){
            if(obj.image){// 이미지일 때
                var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
                str += '<li data-path="' + obj.uploadPath + '" data-uuid="' + obj.uuid + '" data-filename="' + obj.fileName + '" data-type="' + obj.image + '"><div>'
                    + '<span>' + obj.fileName + '</span>'
                    + '<button data-file="' + fileCallPath + '" data-type="image" class="btn btn-warning btn-circle"><i class="fa fa-times"></i></button><br>'
                    + '<img src="/display?fileName=' + fileCallPath + '">'
                    + '</div></li>';
            } else {// 이미지가 아닌 파일일 때
                var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
                var fileLink = fileCallPath.replace(new RegExp('/\\/g'), '/');
                str += '<li data-path="' + obj.uploadPath + '" data-uuid="' + obj.uuid + '" data-filename="' + obj.fileName + '" data-type="' + obj.image + '"><div>'
                    + '<span>' + obj.fileName + '</span>'
                    + '<button data-file="' + fileCallPath + '" data-type="file" class="btn btn-warning btn-circle"><i class="fa fa-times"></i></button><br>'
                    + '<img src="/resources/img/attach.png">'
                    + '</div></li>';
            }
        });
        uploadUL.append(str);
    }

    /* File Upload */
    $('input[type="file"]').change(function(e){// input 변경 감지 -> 파일 업로드
        var formData = new FormData();

        var inputFile = $('input[name="uploadFile"]');

        var files = inputFile[0].files;
        //console.log('value: ' + inputFile[0].value);// [{파일, 파일, ...}]

        for(var i = 0; i < files.length; i++){
            if(!checkExtension(files[i].name, files[i].size)){
                return false;
            }
            formData.append('uploadFile', files[i]);// 같은 이름의 키에 계속 append
        }

        $.ajax({
            url: '/uploadAjaxAction',
            processData: false,
            contentType: false,
            data: formData,
            type: 'post',
            dataType: 'json', // 서버한테 받고 싶은
            success: function(result){// List<AttachFileDTO>
                console.log(result);
                showUploadResult(result);
            }
        });
    });

});