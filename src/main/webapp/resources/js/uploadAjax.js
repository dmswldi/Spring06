$(document).ready(function(){

    /* 업로드 제한 체크 */
    var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
    var maxSize = 5242880; // 5MB

    function checkExtension(fileName, fileSize){
        if(fileSize >= maxSize){
            alert("파일 사이즈 초과");
            return false;
        }

        if(regex.test(fileName)){
            alert("해당 종류의 파일은 업로드할 수 없습니다.");
            return false;
        }
        return true;
    }

    /* 업로드 버튼 클릭 */
    $('#uploadBtn').on('click', function(e){
        var formData = new FormData();

        var inputFile = $('input[name="uploadFile"]');
        var files = inputFile[0].files;// 1st input tag's files

        console.log(files);

        for(var i = 0; i < files.length; i++){
            //alert(i + "번째")
            if(!checkExtension(files[i].name, files[i].size)){
                // .zip, .pdf 동시 첨부 시
                //break;// 1 가까운 반복문(if) 벗어난다, for문 다 돌면서 둘 다 alert 하고 정상적인 걸 formData에 append
                return false; // 0 .zip 만나면 for문 더 안 돌고 함수 탈출

            }
            alert('OK! ' + files[i].name);
            formData.append('uploadFile', files[i]);
        }


        var iter = formData.entries();
        console.log("-----------------");
        console.log(iter.next().value);
        console.log(iter.next().value);
        console.log(iter.next());// true


        $.ajax({
            url: '/uploadAjaxAction',
            processData: false,
            contentType: false,
            data: formData,
            type: 'post',
            dataType: 'json',
            success: function(result, status, xhr){// List<AttachFileDTO>
                console.log(result);// List<AttachFileDTO>
                console.log(status);// success
                console.log(xhr.responseText);// json 객체 [{}] XmlHttpRequest???

                showUploadedFile(result);// ul에 li 붙이고

                $('.uploadDiv').html(cloneObj.html());// 선택된 파일 없음 상태로 초기화
            }
        }).fail(function(xhr, status, err){
            console.log(xhr.responseText);
            console.log(status);// error
            console.log(err);// ''
        });
    });


    /* 이미지와 함께 업로드 파일 보이게 처리 */
    var cloneObj = $('.uploadDiv').clone();
    var uploadResult = $('.uploadResult ul');

    function showUploadedFile(uploadResultArr){
        var str = '';

        $(uploadResultArr).each(function(i, obj){// i: 0~
            if(!obj.image){// 일반 파일 : default icon 처리
                var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);

                str += '<li><a href="/download?fileName=' + fileCallPath + '" download>'
                    + '<img src="/resources/img/attach.png">' + obj.fileName + '</a>'
                    + '<span data-file=\"' + fileCallPath + '\" data-type="file"> x </span>'
                    + '</li>';
            } else {// img : thumbnail 처리
                // uri 한글, 공백 처리
                var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);

                var originPath = obj.uploadPath + '/' + obj.uuid + '_' + obj.fileName;
                //alert('originPath: ' + originPath);
                originPath = originPath.replace(new RegExp(/\\/g), '/');

                // <li><a href='javascript:showImage("  ")'>
                str += '<li><a href=\'javascript:showImage(\"' + originPath + '\")\'>'
                    + '<img src="/display?fileName=' + fileCallPath + '">' + obj.fileName + '</a>'
                    + '<span data-file=\"' + fileCallPath + '\" data-type="image"> x </span>'
                    + '</li>';
                console.log(fileCallPath);
            }
        });

        uploadResult.append(str);
    }

    /* 원본 이미지 클릭 시 hide */
    $('.bigPictureWrapper').on('click', function(e){
        $('.bigPicture').animate({width:'0%', height:'0%'}, 1000);
        setTimeout(() => {
            $(this).hide();
        }, 1000);
    });

    /* 첨부파일 삭제 */
    $('.uploadResult').on('click', 'span', function(e){
        var targetFile = $(this).data('file');// fileCallPath
        var type = $(this).data('type');
        console.log(targetFile);

        var deleteTag = $(this).closest('li');

        $.ajax({
           url: '/deleteFile',
           data: {fileName: targetFile, type: type},
           dataType: 'text',// 서버한테 받을 타입
           type: 'post',
           success: function(result){
               alert(result);
           }
        }).done(function(){
            $(deleteTag).remove();
        });
    });

});

/* 이미지 클릭 시 원본 이미지 보이기 - a 태그에서 호출 가능하도록 돔 레디 전에 작성???541p -------------- */
function showImage(fileCallPath){
    // alert(fileCallPath);
    $('.bigPictureWrapper').css('display', 'flex').show();
    $('.bigPicture')
        .html('<img src="/display?fileName=' + encodeURI(fileCallPath) + '">')
        .animate({width:'100%', height:'100%'}, 1000);
}