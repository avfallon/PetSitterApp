$(go);

function go(){
  $("#addBtn").on("click",function(){
    let form_name = document.getElementById("inputName").value;
    let form_species = document.getElementById("inputSpecies").value;
    let form_size = document.getElementById("inputSize").value;
    let form_temper = document.getElementById("inputTemperament").value;
    let form_breed = document.getElementById("inputBreed").value;
    let form_age = document.getElementById("inputAge").value;
    let form_diet = document.getElementById("inputDiet").value;
    let form_health = document.getElementById("inputHealth").value;
    let form_extra = document.getElementById("inputExtra").value;
    //alert("hello");
    $.ajax({
        method: 'POST',
        url: "./php/addPet.php",
        data: {ownerIDKey: getCookie('userKey'), name: form_name, species: form_species, size: form_size, temper: form_temper, breed: form_breed, age: form_age, diet: form_diet, health: form_health, extra: form_extra},
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
    alert("Pet Added!");
  });

}
