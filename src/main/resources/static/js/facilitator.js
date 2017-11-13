function registerFacilitator() {
    address = getAddress(facilitatorAddress);
    name = $("#name").val();

    $.post("/facilitator",
           {
               'name': name,
               'address': address
           }
    )

    contractInstance.registerFacilitator(name, {from: address},
            function(error, result) {
                if(!error) {
                } else {
                    console.error(error);
                }
                document.getElementById('name').value=''
            }
    )
}
