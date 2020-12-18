//$( go );

function validateLogin(){
    //alert("Inside validate");
    let url = "./php/login.php";
    let form_email = document.getElementById("inputEmailAddress").value;
    let form_password = document.getElementById("inputPassword").value;
    $.post(url, { email: form_email, password: form_password }, function (response)
    {
        if (response == 0){
            //alert("yes");
            window.location.href = "./pet-ownerSitter.html";
        }

         else if (response == 1){
            //alert("not right");
            document.getElementById('error_code').removeAttribute("hidden");
        }

         else
            //alert("nope");
            alert.log("UNEXPECTED ERROR CODE");
    })
    alert("Signing in..");
}

function validateSignup() {
    let url = "./php/createAccount.php";
    let form_first = document.getElementById("inputFirstName").value;
    let form_last = document.getElementById("inputLastName").value;
    let form_email = document.getElementById("inputEmailAddress").value;
    let form_address = document.getElementById("inputAddress").value;
    let form_phone = document.getElementById("inputPhoneNumber").value;
    let form_password = document.getElementById("inputPassword").value;
    let form_confirmpass = document.getElementById("inputConfirmPassword").value;
    let type_account = $('input[name=account]:checked').val();
    $.ajax({
        method: 'POST',
        url: url,
        data: {email: form_email, firstName: form_first, lastName: form_last, email: form_email, address: form_address, phoneNumber: form_phone, account: type_account, password: form_password, confirmpassword: form_confirmpass },
        success: function (response)
        {
            console.log(response + "<= RESPONSE")
            if (response == 0){
                //window.location.href = "./pet-ownerSitter.html";
            }
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

        $.ajax({
            method: 'POST',
            url: './php/getKey.php',
            data: {email: form_email },
            success: function (response)
            {
                console.log(response + "<= RESPONSE")
                if (response == 0)
                    window.location.href = "./login.html";
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
          alert("Account Created! Redirecting to Login.");
}
