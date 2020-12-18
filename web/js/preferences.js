$(go);

function go(){
  if(getCookie('email') != undefined) {

      $.post("./php/getUser.php", {email: getCookie('email')},
          function(json)
      {
        var jsonObject = $.parseJSON( json );
          if(jsonObject != undefined) {
                  for (var key in jsonObject){
                    if(jsonObject.hasOwnProperty(key)){
                      var typeOfAccount = jsonObject[key]["typeOfAccount"];
                      var firstName = jsonObject[key]["firstName"];
                      var lastName = jsonObject[key]["lastName"];
                      var address = jsonObject[key]["address"];
                      var phoneNumber = jsonObject[key]["phoneNumber"];
                      var email = jsonObject[key]["email"];
                    }
                  }
              document.getElementById("inputFirstName").value = firstName;
              document.getElementById("inputLastName").value = lastName;
              document.getElementById("inputEmailAddress").value = email;
              document.getElementById("inputAddress").value = address;
              document.getElementById("inputPhoneNumber").value = phoneNumber;
          }
      })
  }
  $("#addBtn").on("click",function(){
    let form_first = document.getElementById("inputFirstName").value;
    let form_last = document.getElementById("inputLastName").value;
    let form_email = document.getElementById("inputEmailAddress").value;
    let form_address = document.getElementById("inputAddress").value;
    let form_phone = document.getElementById("inputPhoneNumber").value;
    let form_password = document.getElementById("inputPassword").value;
    let form_confirmpass = document.getElementById("inputConfirmPassword").value;
    let type_account = $('input[name=account]:checked').val();
    //alert("hello");
    $.ajax({
        method: 'POST',
        url: './php/editAccount.php',
        data: {ownerIDKey: getCookie('userKey'), mail: form_email, firstName: form_first, lastName: form_last, email: form_email, address: form_address, phoneNumber: form_phone, account: type_account, password: form_password, confirmpassword: form_confirmpass },
        success: function (response)
        {
            console.log(response + "<= RESPONSE")
            if (response == 0)
                window.location.href = "./pet-ownerSitter.html";
            else if (response == 1)
                document.getElementById('error_code').removeAttribute("hidden");
            else {
                window.location.href = "./pet-ownerSitter.html";
            }
        },
        error: function(error) {
            document.getElementById('error_code').removeAttribute("hidden");
            document.getElementById('error_code').innerText = response;
        }
        });
    alert("Account Saved!");

  });

}
