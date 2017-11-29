//var expertAddress = '0xcb68535e193f6cfca90213c3a13bc2519cf27f90'
//var clientAddress = '0xc0a862fbbc7d9396df9a923513b084c33499feee'
//var facilitatorAddress = '0xf2c580b24d6e2179976990889ec445a960362fb7'

//var defaultAddress = clientAddress

var tokenPrice;
var balance;

function startApp(web3) {
    abi = JSON.parse('[{"constant":false,"inputs":[{"name":"_name","type":"string"}],"name":"registerExpert","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"addr","type":"address"}],"name":"getClientInitialized","outputs":[{"name":"init","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_uuid","type":"string"}],"name":"payForResearch","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"getBalance","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"experts","outputs":[{"name":"totalRating","type":"uint256"},{"name":"number","type":"uint256"},{"name":"initialized","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_name","type":"string"}],"name":"registerFacilitator","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"uuid","type":"string"}],"name":"getResearchPrice","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"kill","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"facilitators","outputs":[{"name":"initialized","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"getTokenPrice","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_name","type":"string"}],"name":"registerClient","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"addr","type":"address"}],"name":"getExpertInitialized","outputs":[{"name":"init","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"clients","outputs":[{"name":"initialized","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"researchCount","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"addr","type":"address"}],"name":"getFacilitatorInitialized","outputs":[{"name":"init","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"expertAddress","type":"address"}],"name":"getExpertRating","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_uuid","type":"string"},{"name":"_rate","type":"uint256"},{"name":"_comment","type":"string"}],"name":"rateResearchByFacilitator","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_price","type":"uint256"},{"name":"_uuid","type":"string"},{"name":"_password","type":"string"},{"name":"_zipFileChecksumMD5","type":"string"},{"name":"_clientAddress","type":"address"},{"name":"_enquiryId","type":"uint256"},{"name":"_bidId","type":"uint256"}],"name":"depositResearch","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_uuid","type":"string"}],"name":"getResearch","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"uuid","type":"string"}],"name":"getResearchExpert","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_uuid","type":"string"}],"name":"getResearchRatingByFacilitator","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_enquiryId","type":"uint256"},{"name":"_keywords","type":"string"},{"name":"_bidId0","type":"uint256"},{"name":"_expert0","type":"address"},{"name":"_price0","type":"uint256"},{"name":"_bidId1","type":"uint256"},{"name":"_expert1","type":"address"},{"name":"_price1","type":"uint256"},{"name":"_bidId2","type":"uint256"},{"name":"_expert2","type":"address"},{"name":"_price2","type":"uint256"}],"name":"placeEnquiry","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"expertAddress","type":"address"}],"name":"getExpertTotalRating","outputs":[{"name":"totalRating","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"changeOwner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"users","outputs":[{"name":"name","type":"string"},{"name":"balance","type":"uint256"},{"name":"initialized","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"tokenPriceEther","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"member","type":"address"}],"name":"getMemberBalance","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"uuid","type":"string"}],"name":"withdrawResearch","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[],"name":"buyTokens","outputs":[],"payable":true,"stateMutability":"payable","type":"function"},{"constant":false,"inputs":[{"name":"uuid","type":"string"}],"name":"requestResearch","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_uuid","type":"string"},{"name":"_rate","type":"uint256"},{"name":"_comment","type":"string"}],"name":"rateResearchByClient","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"escrowBalance","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_uuid","type":"string"}],"name":"getResearchRatingByClient","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_name","type":"string"},{"name":"_symbol","type":"string"},{"name":"_decimals","type":"uint8"},{"name":"_totalSupply","type":"uint256"},{"name":"_tokenPriceEther","type":"uint8"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"payable":true,"stateMutability":"payable","type":"fallback"},{"anonymous":false,"inputs":[{"indexed":true,"name":"addr","type":"address"},{"indexed":false,"name":"name","type":"string"}],"name":"onExpertRegistered","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"addr","type":"address"},{"indexed":false,"name":"name","type":"string"}],"name":"onClientRegistered","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"addr","type":"address"},{"indexed":false,"name":"name","type":"string"}],"name":"onFacilitatorRegistered","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"member","type":"address"},{"indexed":false,"name":"tokens","type":"uint256"}],"name":"onTokensBought","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"expert","type":"address"},{"indexed":true,"name":"uuid","type":"string"}],"name":"onResearchDeposited","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"expert","type":"address"},{"indexed":true,"name":"uuid","type":"string"}],"name":"onResearchWithdrawn","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"uuid","type":"string"}],"name":"onResearchPaid","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"enquiryId","type":"uint256"}],"name":"onEnquiryPlaced","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"enquiryId","type":"uint256"},{"indexed":true,"name":"bidId","type":"uint256"},{"indexed":true,"name":"expert","type":"address"},{"indexed":false,"name":"price","type":"uint256"}],"name":"onBidPlaced","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"bidId","type":"uint256"},{"indexed":true,"name":"researchUuid","type":"string"}],"name":"onBidExecuted","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"uuid","type":"string"}],"name":"onResearchSent","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"uuid","type":"string"},{"indexed":false,"name":"rate","type":"uint256"}],"name":"onResearchRatedByClient","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"client","type":"address"},{"indexed":true,"name":"uuid","type":"string"},{"indexed":false,"name":"rate","type":"uint256"}],"name":"onResearchRatedByFacilitator","type":"event"}]')
    WrisxContract = web3.eth.contract(abi);
    contractInstance = WrisxContract.at('0xab8ad265f5c84b1ce2a2f35ba65a13b27a2fa122');
    console.log(web3.eth.accounts[0]);

    contractInstance.getTokenPrice.call(
            function(error, result) {
                if(!error) {
                    tokenPrice=result;
                } else {
                    console.error(error);
                }
            }
    );
}

function getWeb3(callback) {
    var myWeb3;
    if (typeof window.web3 === 'undefined') {
        console.error("Please use a web3 browser");
        myWeb3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"))
    } else {
        myWeb3 = new Web3(window.web3.currentProvider);
    }
    callback(myWeb3);
}

async function waitForTxToBeMined (txHash) {
    let txReceipt
    while (!txReceipt) {
        try {
            txReceipt = await eth.getTransactionReceipt(txHash)
        } catch (err) {
            return console.error(err)
        }
    }
    console.log("Success")
}

$(document).ready(function() {
    getWeb3(startApp);
});

function getAddress() {
    getWeb3(startApp)
    var address = web3.eth.accounts[0]
    if (address === undefined) {
        console.log('address undefined')
        return ''
    }
    return address
}

function showUserBalance() {
    address = getAddress()
    contractInstance.getMemberBalance.call(address,
            function(error, result) {
                if(!error) {
                    balance = result
                    $("#userBalance").text(balance)
                } else {
                    $("#userBalance").text('')
                }
            }
    )
}

function showUserData() {
    address = getAddress()
    $("#userAddress").text(address)
    $.get({
        url: "/user/" + address,
        success: function(data) {
            $("#userName").text(data.name)
        },
        error: function() {
            $("#userName").text('')
        }
    })
}

function showRolePanels() {
    showClientRoleTab()
    showExpertRoleTab()
}

function showExpertRoleTab() {
    address = getAddress()
    $.get({
        url: "/expert/" + address,
        success: function(data) {
            $("#roleExpertTabTitle").text('Expert')
            $("#roleExpertTabs").show()
            $("#registerExpertPanel").hide()
        },
        error: function() {
            $("#roleExpertTabTitle").text('Become an Expert')
            $("#roleExpertTabs").hide()
            $("#registerExpertPanel").show()
            $.get({
                url: "/user/" + address,
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
            })
        }
    })
    showUserData()
}

function showClientRoleTab() {
    address = getAddress()
    $.get({
        url: "/client/" + address,
        success: function(data) {
            $("#roleClientTabTitle").text('Client')
            $("#roleClientTabs").show()
            $("#registerClient").hide()
        },
        error: function() {
            $("#roleClientTabTitle").text('Become a Client')
            $("#roleClientTabs").hide()
            $("#registerClient").show()
            $.get({
                url: "/user/" + address,
                success: function(data) {
                    $("#clientName").val(data.name)
                    $("#clientEmailAddress").val(data.emailAddress)
                    $("#clientName").prop('disabled', true)
                    $("#clientEmailAddress").prop('disabled', true)
                },
                error: function() {
                    $("#clientName").val('')
                    $("#clientEmailAddress").val('')
                    $("#clientName").prop('disabled', false)
                    $("#clientEmailAddress").prop('disabled', false)
                }
            })
        }
    })
    showUserData()
}

function showDateTime(timestamp) {
    date = new Date(timestamp)

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
