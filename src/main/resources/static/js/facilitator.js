function registerFacilitator() {
    address = getAddress(facilitatorAddress);
    name = $("#name").val();

    $.post("/facilitator",
        {
            'name': name,
            'address': address,
            'emailAddress': '',
            'comment': ''
        }
    )
    .fail(function(error) {
        handleError(error);
    });

    contractInstance.registerFacilitator({from: address},
        function(error, result) {
            if(!error) {

            } else {
                console.error(error);
            }
            document.getElementById('name').value=''
        }
    )
}
