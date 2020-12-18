$( go );

function go( )
{
  var table = document.getElementById("dataTable");
  $.ajax( { url: "./php/getPets.php", success: function( json ) {
    var jsonObject = $.parseJSON( json );
    var petsHTML = "";
    console.log(jsonObject);
    for (var key in jsonObject){
      if(jsonObject.hasOwnProperty(key)){
        petsHTML += "<tr>";
        petsHTML += "<td>" + jsonObject[key]["name"] + "</td>";
        petsHTML += "<td>" + jsonObject[key]["species"] + "</td>";
        petsHTML += "<td>" + jsonObject[key]["age"] + "</td>";
        petsHTML += "<td>" + jsonObject[key]["breed"] + "</td>";
        petsHTML += "<td>" + jsonObject[key]["size"] + "</td>";
        petsHTML += "<td>";
        petsHTML += "</tr>";
      }
    }
    $("#dataTable tbody").html(petsHTML);
  }});
}
