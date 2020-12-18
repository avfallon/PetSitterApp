$(go);

function go(){
  var table = document.getElementById("dataTable");
  var acceptedObject = "";
  if(getCookie('userKey') != undefined) {

    $.ajax({
        method: 'POST',
        url: './php/getOpenJobs.php',
        data: {ownerIDKey: getCookie('userKey')},
        success: function (json)
        {
        acceptedObject = $.parseJSON( json );
        var acceptedHTML = "";
          if(acceptedObject != undefined) {
                  for (var key in acceptedObject){
                    if(acceptedObject.hasOwnProperty(key)){
                      acceptedHTML += "<tr data-jobID="+acceptedObject[key]["jobID"]+">";
                      acceptedHTML += "<td>" + acceptedObject[key]["name"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["species"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["age"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["breed"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["size"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["startDate"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["endDate"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["sleepover"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["jobDetails"] + "</td>";
                      acceptedHTML += "<td>" + acceptedObject[key]["ownIDKey"] + "</td>";
                      acceptedHTML += "<td><button class='btn btn-primary btn-xs'>Accept Job</button></td>";
                      acceptedHTML += "<td>";
                      acceptedHTML += "</tr>";
                    }
                  }
                $("#dataTable tbody").html(acceptedHTML);
              //}
              //document.getElementById("profile").removeAttribute("hidden");
          }
        },
        async:false
      })
  }
  $('.btn-xs').on("click",function(){
    $(this).parents('tr').css('background', 'pink');
    id = $(this).parents('tr').data('jobid');
    //alert(id);
    $.ajax({
        method: 'POST',
        url: './php/acceptJob.php',
        data: {sitterIDKey: getCookie('userKey'), jobID: $(this).parents('tr').data('jobid') },
        success: function (response)
        {
            console.log(response + "<= RESPONSE")
            if (response == 0)
                window.location.href = "./pet-ownerSitter.html";
            else if (response == 1)
                document.getElementById('error_code').removeAttribute("hidden");
            else {
                window.location.href = "./index.php";
            }
        },
        error: function(error) {
            document.getElementById('error_code').removeAttribute("hidden");
            document.getElementById('error_code').innerText = response;
        }
        });
  });

}
