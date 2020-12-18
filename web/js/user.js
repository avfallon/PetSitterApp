$(go);

function go() {
    if(getCookie('email') != undefined) {

        $.post("./php/getUser.php", {email: getCookie('email')},
            function(json)
        {
          var jsonObject = $.parseJSON( json );
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
                        var typeOfAccount = jsonObject[key]["typeOfAccount"];
                        var firstName = jsonObject[key]["firstName"];
                        var lastName = jsonObject[key]["lastName"];
                      }
                    }
                //}
                document.getElementById("user").innerHTML = "Logged in as: "+ "<br />" + firstName + " "+lastName;
                //document.getElementById("profile").removeAttribute("hidden");
                if (typeOfAccount == 1){
                  $(document).ready(function(){
                      $("#tokensTitle").hide();
                      $("#numberOfTokens").hide();
                      $("#tokenBtn").hide();
                      $("#table").hide();
                      $("#jobsTable").hide();
                  });
                }
                else if(typeOfAccount == 2){

                }
            }
        })
    }
    else{
      window.location.href = "./login.html";
    }

}
