$(document).ready(function(){
    /* Submit btn Click */
    /* AttachFileDTO hidden 처리 */
    var formObj = $('form[role="form"]');

    $('button[type="submit"]').on('click', function(e){
       e.preventDefault();
       console.log('submit clicked');

       var str = '';

       $('.uploadResult ul li').each(function(i, obj){
           var jobj = $(obj);// ???
           console.dir(jobj);// -> li, displays an interactive list of the properties / S.fn.init(1) 0: li

           str += '<input type="hidden" name="attachList[' + i + '].fileName" value="' + jobj.data('filename') + '">'
               + '<input type="hidden" name="attachList[' + i + '].uuid" value="' + jobj.data('uuid') + '">'
               + '<input type="hidden" name="attachList[' + i + '].uploadPath" value="' + jobj.data('path') + '">'
               + '<input type="hidden" name="attachList[' + i + '].fileType" value="' + jobj.data('type') + '">';
       });
       formObj.append(str).submit();// form 안 마지막에 append
   });

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

    /* File Delete */
    $('.uploadResult').on('click', 'button', function(){
       console.log('delete file');

       var targetFile = $(this).data('file');
       var type = $(this).data('type');

       var targetLi = $(this).closest('li');

       $.ajax({
          url: '/deleteFile',
          data: {fileName: targetFile, type: type},
          type: 'post',
          success: function(result){
              alert(result);
              targetLi.remove();
          }
       });
    });

});