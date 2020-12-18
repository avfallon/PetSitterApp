$(go);

function go(){
  var table = document.getElementById("inputPets");
  if(getCookie('userKey') != undefined) {

      $.post("./php/getUsersPets.php", {ownerIDKey: getCookie('userKey')},
          function(json)
      {
        jsonObject = $.parseJSON( json );
        var optionsHTML = "";
          if(jsonObject != undefined) {
                  for (var key in jsonObject){
                    if(jsonObject.hasOwnProperty(key)){
                      optionsHTML += "<option value="+jsonObject[key]["petIDKey"]+">"+ jsonObject[key]["name"] +"</option>";
                    }
                  }
                $("#inputPets").html(optionsHTML);
              //}
              //document.getElementById("profile").removeAttribute("hidden");
          }
      })
    }
    $("#submitPost").on("click",function(){
      //alert("submitting post");
      let url = "./php/createJob.php";
      let start_date = document.getElementById("inputStartDate").value;
      let end_date = document.getElementById("inputEndDate").value;
      let job_details = document.getElementById("inputJobDetails").value;
      var sleepover = "";
      if (document.getElementById("inputSleepover").checked){
        sleepover = "1";
      }
      else{
        sleepover = "0";
      }
      let petIDKey = document.getElementById("inputPets").value;

      $.ajax({
          method: 'POST',
          url: url,
          data: {startDate: start_date, endDate: end_date, jobDetails: job_details, sleepover: sleepover, petIDKey: petIDKey, ownerIDKey: getCookie('userKey') },
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
          //alert("Posting Pet");

          return false;
    })
}
