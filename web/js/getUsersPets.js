$( go );

function go( )
{
  var table = document.getElementById("dataTable");
  var jsonObject = "";
  if(getCookie('userKey') != undefined) {

      $.post("./php/getUsersPets.php", {ownerIDKey: getCookie('userKey')},
          function(json)
      {
        jsonObject = $.parseJSON( json );
        var petsHTML = "";
          if(jsonObject != undefined) {
              //if (window.location.href.includes("index.php")) {
                  //document.getElementById('welcome').removeAttribute("hidden");
                  //document.getElementById("full_name").innerText = response;
                  //document.getElementById("login_container").setAttribute("hidden", "true");
                  //$(document).ready(function(){
                    //  $("#login_container").hide();
                  //});
                  for (var key in jsonObject){
                    if(jsonObject.hasOwnProperty(key)){
                      petsHTML += "<tr>";
                      petsHTML += "<td>" + jsonObject[key]["name"] + "</td>";
                      petsHTML += "<td>" + jsonObject[key]["species"] + "</td>";
                      petsHTML += "<td>" + jsonObject[key]["age"] + "</td>";
                      petsHTML += "<td>" + jsonObject[key]["breed"] + "</td>";
                      petsHTML += "<td>" + jsonObject[key]["size"] + "</td>";
                      petsHTML += "<td><button class='btn btn-primary btn-xs'>Delete</button></td>";
                      petsHTML += "<td>";
                      petsHTML += "</tr>";
                    }
                  }
                $("#dataTable tbody").html(petsHTML);
                if(getCookie('account') == "2"){
                  $("#dataTable").hide();
                  $("#head2").hide();
                  $("#CreatePost").hide();
                }
              //}
              //document.getElementById("profile").removeAttribute("hidden");
          }
      })

      $.post("./php/getPostedJobs.php", {ownerIDKey: getCookie('userKey')},
          function(json)
      {
        var jobsObject = $.parseJSON( json );
        var jobsHTML = "";
          if(jobsObject != undefined) {
              //if (window.location.href.includes("index.php")) {
                  //document.getElementById('welcome').removeAttribute("hidden");
                  //document.getElementById("full_name").innerText = response;
                  //document.getElementById("login_container").setAttribute("hidden", "true");
                  //$(document).ready(function(){
                    //  $("#login_container").hide();
                  //});
                  for (var key in jobsObject){
                    if(jobsObject.hasOwnProperty(key)){
                      jobsHTML += "<tr>";
                      jobsHTML += "<td>" + jobsObject[key]["name"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["species"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["age"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["breed"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["size"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["startDate"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["endDate"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["sleepover"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["jobDetails"] + "</td>";
                      jobsHTML += "<td>" + jobsObject[key]["sitterIDKey"] + "</td>";
                      jobsHTML += "<td><button class='btn btn-primary btn-xs'>Cancel</button></td>";
                      jobsHTML += "<td>";
                      jobsHTML += "</tr>";
                    }
                  }
                $("#postedTable tbody").html(jobsHTML);
                if(getCookie('account') == "2"){
                  $("#postedTable").hide();
                  $("#head3").hide();
                }
              //}
              //document.getElementById("profile").removeAttribute("hidden");
          }
      })

      $.post("./php/getAcceptedJobs.php", {ownerIDKey: getCookie('userKey')},
          function(json)
      {
        var acceptedObject = $.parseJSON( json );
        var acceptedHTML = "";
          if(acceptedObject != undefined) {
              //if (window.location.href.includes("index.php")) {
                  //document.getElementById('welcome').removeAttribute("hidden");
                  //document.getElementById("full_name").innerText = response;
                  //document.getElementById("login_container").setAttribute("hidden", "true");
                  //$(document).ready(function(){
                    //  $("#login_container").hide();
                  //});
                  for (var key in acceptedObject){
                    if(acceptedObject.hasOwnProperty(key)){
                      acceptedHTML += "<tr>";
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
                      acceptedHTML += "<td><button class='btn btn-primary btn-xs'>Cancel</button></td>";
                      acceptedHTML += "<td>";
                      acceptedHTML += "</tr>";
                    }
                  }
                $("#jobsTable tbody").html(acceptedHTML);
                if(getCookie('account') == "1"){
                  $("#jobsTable").hide();
                  $("#head1").hide();
                  $("#NewJobs").hide();
                }
              //}
              //document.getElementById("profile").removeAttribute("hidden");
          }
      })

    $("#addPet").on("click",function(){
      window.location.href = "./addPet.html";
    });

    //window.location.reload();
    $("#signOut").on("click", function(){
      if (getCookie('email') != "")
      {
          deleteCookie('email');
          deleteCookie('userKey');
          deleteCookie('account');
          window.location.href = "./homepage.html";
      }
    });
  }
}
