var tokenPrice;
var balance;
const contextPath = "/wwa";
const interval = 500;

function startApp(web3) {
    abi = JSON.parse('[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"addr","type":"address"}],"name":"getClientInitialized","outputs":[{"name":"init","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_uuid","type":"string"}],"name":"payForResearch","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"getBalance","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"experts","outputs":[{"name":"totalRating","type":"uint256"},{"name":"number","type":"uint256"},{"name":"initialized","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"_totalSupply","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_name","type":"string"},{"name":"_secret","type":"string"}],"name":"registerFacilitator","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"uuid","type":"string"}],"name":"getResearchPrice","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"kill","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_name","type":"string"},{"name":"_secret","type":"string"}],"name":"registerClient","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"facilitators","outputs":[{"name":"initialized","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"getTokenPrice","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"getSecret","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"addr","type":"address"}],"name":"getExpertInitialized","outputs":[{"name":"init","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"clients","outputs":[{"name":"initialized","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"researchCount","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"addr","type":"address"}],"name":"getFacilitatorInitialized","outputs":[{"name":"init","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"expertAddress","type":"address"}],"name":"getExpertRating","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_uuid","type":"string"},{"name":"_rate","type":"uint256"},{"name":"_comment","type":"string"}],"name":"rateResearchByFacilitator","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_price","type":"uint256"},{"name":"_uuid","type":"string"},{"name":"_password","type":"string"},{"name":"_zipFileChecksumMD5","type":"string"},{"name":"_clientAddress","type":"address"},{"name":"_enquiryId","type":"uint256"},{"name":"_bidId","type":"uint256"}],"name":"depositResearch","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_name","type":"string"},{"name":"_secret","type":"string"}],"name":"registerExpert","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_uuid","type":"string"}],"name":"getResearch","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"getOwner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_uuid","type":"string"}],"name":"getResearchRatingByFacilitator","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_enquiryId","type":"uint256"},{"name":"_keywords","type":"string"},{"name":"_bidId0","type":"uint256"},{"name":"_expert0","type":"address"},{"name":"_price0","type":"uint256"},{"name":"_bidId1","type":"uint256"},{"name":"_expert1","type":"address"},{"name":"_price1","type":"uint256"},{"name":"_bidId2","type":"uint256"},{"name":"_expert2","type":"address"},{"name":"_price2","type":"uint256"}],"name":"placeEnquiry","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"expertAddress","type":"address"}],"name":"getExpertTotalRating","outputs":[{"name":"totalRating","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"changeOwner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"users","outputs":[{"name":"name","type":"string"},{"name":"balance","type":"uint256"},{"name":"initialized","type":"uint256"},{"name":"secret","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_amount","type":"uint256"}],"name":"transfer","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"tokenPriceEther","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"uuid","type":"string"}],"name":"withdrawResearch","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[],"name":"buyTokens","outputs":[],"payable":true,"stateMutability":"payable","type":"function"},{"constant":true,"inputs":[{"name":"uuid","type":"string"}],"name":"requestResearch","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_uuid","type":"string"},{"name":"_rate","type":"uint256"},{"name":"_comment","type":"string"}],"name":"rateResearchByClient","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"escrowBalance","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_uuid","type":"string"}],"name":"getResearchRatingByClient","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_name","type":"string"},{"name":"_symbol","type":"string"},{"name":"_decimals","type":"uint8"},{"name":"_totalSupply","type":"uint256"},{"name":"_tokenPriceEther","type":"uint8"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"payable":true,"stateMutability":"payable","type":"fallback"},{"anonymous":false,"inputs":[{"indexed":true,"name":"addr","type":"address"},{"indexed":false,"name":"name","type":"string"}],"name":"ExpertRegistered","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"addr","type":"address"},{"indexed":false,"name":"name","type":"string"}],"name":"ClientRegistered","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"addr","type":"address"},{"indexed":false,"name":"name","type":"string"}],"name":"FacilitatorRegistered","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"user","type":"address"},{"indexed":false,"name":"tokens","type":"uint256"}],"name":"TokensBought","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"expert","type":"address"},{"indexed":true,"name":"uuid","type":"string"}],"name":"ResearchDeposited","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"expert","type":"address"},{"indexed":true,"name":"uuid","type":"string"}],"name":"ResearchWithdrawn","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"uuid","type":"string"}],"name":"ResearchPaid","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"enquiryId","type":"uint256"}],"name":"EnquiryPlaced","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"enquiryId","type":"uint256"},{"indexed":true,"name":"bidId","type":"uint256"},{"indexed":true,"name":"expert","type":"address"},{"indexed":false,"name":"price","type":"uint256"}],"name":"BidPlaced","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"bidId","type":"uint256"},{"indexed":true,"name":"researchUuid","type":"string"}],"name":"BidExecuted","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"uuid","type":"string"}],"name":"ResearchSent","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"uuid","type":"string"},{"indexed":false,"name":"rate","type":"uint256"}],"name":"ResearchRatedByClient","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"uuid","type":"string"},{"indexed":false,"name":"rate","type":"uint256"}],"name":"ResearchRatedByFacilitator","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","type":"event"}]');
    WrisxContract = web3.eth.contract(abi);
    contractInstance = WrisxContract.at('0xa6c1e1916cb43ad5070a1aaeadc3c6e7c568285e');
    console.log(web3.eth.accounts[0]);

    contractInstance.getTokenPrice.call(
        function(error, result) {
            if(error) {
                console.error(error);
            } else {
                tokenPrice=result;
            }
        }
    );

//    $.get(contextPath + "/nonce?address=" + web3.eth.accounts[0],
//        function(data) {
//            if (data != undefined) {
//                var nonce = data;
//                contractInstance.getSecret.call({from: web3.eth.accounts[0]},
//                    function(error, result) {
//                        if(error) {
//                            console.log(error);
//                        } else {
//                            var hash = md5(result + nonce);
//                            $.post(contextPath + "/authorise",
//                                {
//                                    'hash': hash
//                                },
//                                function(data) {
//                                    console.log(data);
//                                }
//                            )
//                        }
//                    }
//                );
//            }
//        }
//    );
    $("#anonymousResearchItemsPanel").hide();
    $("#anonymousResearchItemPanel").hide();
    $("#anonymousExpertsPanel").hide();
    $("#anonymousExpertPanel").hide();
}

function handleError(error) {
    bootbox.alert(error.responseJSON.message + " " +
    error.responseJSON.error + " " +
    error.responseJSON.status + " " +
    error.responseJSON.path);
}

function handleErrorResponse(path, error) {
    var msg = error.statusText + " " + error.status + " " + path;
    console.log(msg);
    bootbox.alert(msg);
}

function confirmTransaction(path, transactionHash) {
    var transactionHashData = JSON.stringify({
                                              'transactionHash': transactionHash
                                             });
    $.ajax({
            url: path,
            type: 'put',
            data: transactionHashData,
            headers: {
                'Content-Type': 'application/json'
            },
            error: function(error) {
                handleErrorResponse(path, error);
            }
    });
}

function commitTransaction(path, transactionHash, func) {
    var transactionHashData = JSON.stringify({
                                              'transactionHash': transactionHash
                                             });
    $.ajax({
        url: path,
        type: 'put',
        data: transactionHashData,
        headers: {
            'Content-Type': 'application/json'
        },
        success: func,
        error: function(error) {
            handleErrorResponse(path, error);
        }
    });
}

function rollbackTransaction(path, error, func) {
    console.log(error);
    $.ajax({
        url: path,
        type: 'delete',
        error: function(error) {
            handleErrorResponse(path, error);
        }
    });
}

function getWeb3(callback) {
    var Web3 = require('web3');
    var myWeb3;
    if (typeof window.web3 === 'undefined') {
        console.error("Please use a web3 browser");
        myWeb3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"))
    } else {
        myWeb3 = new Web3(window.web3.currentProvider);
    }
    callback(myWeb3);
}

$(document).ready(function() {
    getWeb3(startApp);
});

function getAddress() {
    var address = web3.eth.accounts[0]
    if (address === undefined) {
        console.log('address undefined')
        return ''
    }
    return address
}

function waitForTransactionToBeMined(txHash, func, param1, param2) {
    const transactionReceiptAsync = function(resolve, reject) {
        web3.eth.getTransactionReceipt(txHash, (error, receipt) => {
            if (error) {
                reject(error);
            } else if (receipt == null) {
                setTimeout(
                    () => transactionReceiptAsync(resolve, reject),
                    interval);
            } else {
                resolve(receipt);
                if (param1 === undefined) {
                    setTimeout(
                        () => func(),
                        interval * 4
                    );
                } else if (param2 === undefined) {
                    setTimeout(
                        () => func(param1),
                        interval * 4
                    );
                } else {
                    setTimeout(
                        () => func(param1, param2),
                        interval * 4
                    );
                }
            }
        });
    };
    return new Promise(transactionReceiptAsync);
};

function showMemberBalance() {
    address = getAddress()
    if (address === undefined) {
        $("#tokenBalance").text('')
        $("#etherBalance").text('')
    } else {
        contractInstance.balanceOf.call(address,
                function(error, result) {
                    if(!error) {
                        balance = result
                        $("#tokenBalance").text(balance)
                    } else {
                        $("#tokenBalance").text('')
                    }
                }
        )
        web3.eth.getBalance(address, function(error, result) {
            if (error) {
                console.log(error);
            } else {
                $("#etherBalance").text(web3.fromWei(result, 'ether'))
            }
        })
    }
}

function showMemberData() {
    address = getAddress()
    if (address === undefined || address === "") {
        $("#memberAddress").text("-");
    } else {
        $("#memberAddress").text(address);
        $.get({
            url: contextPath + "/user/" + address,
            success: function(data) {
                $("#memberName").text(data.name);
            },
            error: function() {
                $("#memberName").text('');
            }
        });
    }
}

function showRolePanels() {
    showClientRoleTab()
    showExpertRoleTab()
}

function showExpertRoleTab() {
    address = getAddress()
    $.get({
        url: contextPath + "/expert/" + address,
        success: function(data) {
            $("#roleExpertTabTitle").text('Expert');
            $("#roleExpertTabs").show();
            $("#registerExpertPanel").hide();
        },
        error: function() {
            $("#roleExpertTabTitle").text('Become an Expert')
            $("#roleExpertTabs").hide()
            $("#registerExpertPanel").show()
            $.get({
                url: contextPath + "/user/" + address,
                success: function(data) {
                    $("#expertName").val(data.name)
                    $("#expertEmailAddress").val(data.emailAddress)
                    $("#expertName").prop('disabled', true)
                    $("#expertEmailAddress").prop('disabled', true)
                },
                error: function() {
                    $("#expertName").val('')
                    $("#expertEmailAddress").val('')
                    $("#expertName").prop('disabled', false)
                    $("#expertEmailAddress").prop('disabled', false)
                }
            });
        }
    });

    showMemberData();
}

function showClientRoleTab() {
    address = getAddress();

    $.get({
        url: contextPath + "/client/" + address,
        success: function(data) {
            $("#roleClientTabTitle").text('Client');
            $("#roleClientTabs").show();
            $("#registerClientPanel").hide();
        },
        error: function() {
            $("#roleClientTabTitle").text('Become a Client');
            $("#roleClientTabs").hide();
            $("#registerClientPanel").show();
            $.get({
                url: contextPath + "/user/" + address,
                success: function(data) {
                    $("#clientName").val(data.name);
                    $("#clientEmailAddress").val(data.emailAddress);
                    $("#clientName").prop('disabled', true);
                    $("#clientEmailAddress").prop('disabled', true);
                },
                error: function() {
                    $("#clientName").val('');
                    $("#clientEmailAddress").val('');
                    $("#clientName").prop('disabled', false);
                    $("#clientEmailAddress").prop('disabled', false);
                }
            })
        }
    });

    showMemberData();
}

function showDateTime(timestamp) {
    date = new Date(timestamp);

    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var min = date.getMinutes();
    var sec = date.getSeconds();

    month = (month < 10 ? "0" : "") + month;
    day = (day < 10 ? "0" : "") + day;
    hour = (hour < 10 ? "0" : "") + hour;
    min = (min < 10 ? "0" : "") + min;
    sec = (sec < 10 ? "0" : "") + sec;

    return date.getFullYear() + "-" + month + "-" + day + " " +  hour + ":" + min + ":" + sec;
}

function getSecret() {
    return '' + (Math.random() * 100000000000000000 + 1);
}

jQuery.each( [ "put", "delete" ], function( i, method ) {
  jQuery[ method ] = function( url, data, callback, type ) {
    if ( jQuery.isFunction( data ) ) {
      type = type || callback;
      callback = data;
      data = undefined;
    }

    return jQuery.ajax({
      url: url,
      type: method,
      dataType: type,
      data: data,
      success: callback
    });
  };
});
