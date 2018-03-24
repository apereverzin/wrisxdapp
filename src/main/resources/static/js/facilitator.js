function registerFacilitator() {
    address = getAddress();
    var name = $("#facilitatorName").val();
    var emailAddress = $("#facilitatorEmailAddress").val();
    var password = $("#facilitatorPassword").val();
    var keywords = $("#facilitatorKeywords").val();
    var profileLink = $("#facilitatorProfileLink").val();
    var websiteLink = $("#facilitatorWebsiteLink").val();
    var description = $("#facilitatorDescription").val();

    var facilitatorRequestData = JSON.stringify({
                                            'name': name,
                                            'address': address,
                                            'emailAddress': emailAddress,
                                            'password': password,
                                            'profileLink': profileLink,
                                            'websiteLink': websiteLink,
                                            'keywords': keywords,
                                            'description': description
                                            });

    $.ajax({
        url: contextPath + '/facilitator',
        type: 'POST',
        data: facilitatorRequestData,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            contractInstance.registerFacilitator({from: address},
                function(error, result) {
                    if(error) {
                        rollbackTransaction(contextPath + "/facilitator/" + address,
                            error,
                            function(data) {
                                showMemberData();
                                showMemberBalance();
                            }
                        );
                    } else {
                        confirmTransaction(contextPath + '/facilitator/' + address + '/confirm',
                                           result);
                        $("#facilitatorName").val('');
                        $("#facilitatorEmailAddress").val('');
                        $("#facilitatorPassword").val('');
                        $("#facilitatorRepeatPassword").val('');
                        $("#facilitatorProfileLink").val('');
                        $("#facilitatorWebsiteLink").val('');
                        $("#facilitatorKeywords").val('');
                        $("#facilitatorDescription").val('');
                        waitForTransactionToBeMined(result, facilitatorRegistered, result)
                    }
                }
            )
        },
        error: function(error) {
            handleError(error);
        }
    });
}

function facilitatorRegistered(transactionHash) {
    commitTransaction(contextPath + '/facilitator/' + address + '/commit',
                      transactionHash,
                      function(data) {
                          showMemberData();
                          showMemberBalance();
                          showFacilitatorRoleTab();
                      }
    );
}
