var enquiryBids
var globalEnquiryId

function clientResearchItemsTabClicked() {
    address = getAddress();

    if (address != undefined && address != "") {
        $.get({
            url: contextPath + "/research/client/" + address + "/keywords",
            success: function(data) {
                showClientResearchItems(data)
            },
            error: function(error) {
                if (error.responseJSON.status != 404) {
                    handleError(error);
                }
            }
        });
    }

    showMemberData();
    showMemberBalance();
    $("#clientResearchItemPanel").html('');
}

function clientPurchasesTabClicked() {
    address = getAddress();

    $.get(contextPath + "/purchase/client/" + address,
        function(data) {
            showClientPurchases(data)
        }
    )
    .fail(function(error) {
        handleError(error);
    });

    showMemberData()
    showMemberBalance()
}

function clientEnquiriesTabClicked() {
    getClientEnquiries()
    showMemberData()
    showMemberBalance()
}

function registerClient() {
    address = getAddress();
    var name = $("#clientName").val();
    var emailAddress = $("#clientEmailAddress").val();
    var description = $("#clientDescription").val();
    var secret = getSecret();

    var clientData = JSON.stringify({
                                     'name': name,
                                     'address': address,
                                     'emailAddress': emailAddress,
                                     'description': description,
                                     'secret': secret
                                     });
    var path = contextPath + '/client';
    $.ajax({
        url: path,
        type: 'post',
        data: clientData,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            contractInstance.registerClient(secret, {from: address},
                function(error, result) {
                    if(error) {
                        rollbackTransaction(contextPath + "/client/" + address,
                            error,
                            function(data) {
                                showMemberData();
                                showMemberBalance();
                            }
                        );
                    } else {
                        confirmTransaction(contextPath + '/client/' + address + '/confirm',
                                           result);
                        $("#clientName").val('');
                        $("#clientEmailAddress").val('');
                        $("#clientDescription").val('');
                        showMemberData();
                        showMemberBalance();
                        showClientRoleTab();
                        waitForTransactionToBeMined(result, clientRegistered, result);
                    }
                }
            )
        },
        error: function(error) {
            handleErrorResponse(path, error);
        }
    });
}

function clientRegistered(transactionHash) {
    commitTransaction(contextPath + '/client/' + address + '/commit',
                      transactionHash,
                      function(data) {
                          showMemberData();
                          showMemberBalance();
                          showExpertRoleTab();
                      }
    );
}

function viewClientResearchItem(uuid) {
    $.get(contextPath + "/research/" + uuid,
        function(data) {
            var text = '<table style="width:100%">'
            text = text.concat('<tr><th>Title</th><td>' + data.title + '</td></tr>')
            text = text.concat('<tr><th>Description</th><td>' + data.description + '</td></tr>')
            text = text.concat('<tr><th>Key words</th><td>' + data.keywords + '</td></tr>')
            text = text.concat('<tr><th>Price</th><td>' + data.price + '</td></tr>')
            text = text.concat('<tr><th>MD5</th><td>' + data.checksum + '</td></tr>')
            text = text.concat('<tr><th>Expert</th><td>' + data.expert.name + '</td></tr>')
            text = text.concat('</table>')
            $("#clientResearchItemPanel").html(text)
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function buyTokens() {
    amount = $("#clientBuyTokensAmount").val() * tokenPrice;
    address = getAddress();

    contractInstance.buyTokens({from: address, value: amount},
            function(error, result) {
                if(error) {
                    console.log(error);
                } else {
                    $("#clientBuyTokensAmount").val('')
                    waitForTransactionToBeMined(result, showMemberBalance);
                }
            }
    );
}

function payForResearch(uuid) {
    address = getAddress();

    var purchaseData = JSON.stringify({
                                       'address': address,
                                       'uuid': uuid
                                      });
    var path = contextPath + '/purchase';
    $.ajax({
        url: path,
        type: 'post',
        data: purchaseData,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(data) {
            contractInstance.payForResearch(uuid, {from: address},
                function(error, result) {
                    if(error) {
                        rollbackTransaction(contextPath + '/purchase/' + data.id,
                            error,
                            function(data) {}
                        );
                    } else {
                        confirmTransaction(contextPath + '/purchase/' + data.id + '/confirm',
                                           result);
                        showMemberBalance()
                        waitForTransactionToBeMined(result, purchasePaid, data.id, result);
                    }
                }
            );
        },
        error: function(error) {
            handleErrorResponse(path, error);
        }
    });
}

function purchasePaid(purchaseId, transactionHash) {
    commitTransaction(contextPath + '/purchase/' + purchaseId + '/commit',
                      transactionHash,
                      function(data) {
                          showMemberData();
                          showMemberBalance();
                          showExpertRoleTab();
                      }
    );
}

function getResearchPassword(fileName) {
    address = getAddress();

    $.get(contextPath + '/research/client/' + address + '/password/' + fileName,
        function(data) {
            bootbox.alert('<b>Password:</b> ' + data);
        }
    );

//    contractInstance.getResearch.call(fileName, {from: address},
//        function(error, result) {
//            if(error) {
//                console.log(error);
//            } else {
//                bootbox.alert('<b>Password:</b> ' + result);
//            }
//        }
//    );
}

function showClientResearchItems(data) {
    var items = '<table style="width:100%">\n' +
    '<thead><tr>\n' +
    '<th>Title</th><th>Expert</th><th>Price</th>\n' +
    '<th></th><th></th><th></th>\n' +
    '</tr></thead>\n' +
    '<tbody>\n';
    $.each(data, function(val) {
        items = items.concat(
        '<tr>\n' +
        '<td>' + data[val].title + '</td>\n' +
        '<td>' + data[val].expert.name + '</td>\n' +
        '<td>' + data[val].price + '</td>\n' +
        '<td>' + '<a href="#" onclick="viewClientResearchItem(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">View</a>' + '</td>\n'
        );
        if (data[val].purchase == null) {
            items = items.concat(
            '<td>' + '<a href="#" onclick="payForResearch(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">Pay</a>' + '</td>\n'
            );
        } else {
            items = items.concat(
            '<td>' + '<a href="#" onclick="getResearchPassword(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">Password</a>' + '</td>'
            );
        }
        items = items.concat(
        '<td>' + '<a href="' + contextPath + '/downloadFile/' + data[val].uuid + '" class="btn btn-primary">Download</a>' + '</td>' +
        '</tr>\n'
        );
    });
    items.concat('</tbody></table>')
    $("#clientResearchItemsPanel").html(items)
    showMemberBalance()
}

function showClientEnquiries(data) {
    var items = '<table style="width:100%">\n' +
    '<thead><tr>\n' +
    '<th>Key words</th><th>Description</th>\n' +
    '<th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>\n' +
        '<td>' + data[val].keywords + '</td>\n' +
        '<td>' + data[val].description + '</td>\n' +
        '<td>' + '<a href="#" onclick="getEnquiryBids(' + data[val].id +
                    ')" class="btn btn-primary">Bids</a>' + '</td>\n' +
        '</tr>\n'
        );
    });
    items = items.concat('</tbody></table>\n');

    $("#clientEnquiriesPanel").html(items);

    showMemberBalance();
}

function showClientPurchases(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Title</th><th>Key words</th><th>Price</th><th>Expert</th><th>Date</th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].research.title + '</td>' +
        '<td>' + data[val].research.keywords + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + data[val].expert.name + '</td>' +
        '<td>' + showDateTime(data[val].timestamp) + '</td>')
        items = items.concat('</tr>\n')
    });
    items = items.concat('</tbody></table>')

    $("#clientPurchasesPanel").html(items)

    showMemberBalance()
}

function showEnquiryBids(enquiryId, data) {
    var items = '<table style="width:100%">\n' +
    '<thead><tr>\n' +
    '<th>Bid</th><th>Comment</th><th>Expert</th>\n' +
    '<th></th><th></th>\n' +
    '</tr></thead>\n' +
    '<tbody>';
    var submitted = false;
    $.each(data, function(val) {
        if (data[val].selected) {
            submitted = true;
        }
    });

    enquiryBids = new Array();
    $.each(data, function(val) {
        var enquiryBidCheckboxId = 'bid' + data[val].id;
        if (enquiryBids.length > 0) {
            enquiryBids = enquiryBids.concat(',');
        }
        var enquiryBid = {
            enquiryBidCheckboxId: enquiryBidCheckboxId,
            bidId: data[val].id,
            expert: data[val].expert.address,
            price: data[val].price
        };
        enquiryBids.push(enquiryBid);
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + data[val].comment + '</td>' +
        '<td>' + data[val].expert.name + '</td>')
        if (!submitted) {
            items = items.concat('<td><input type="checkbox" id="' + enquiryBidCheckboxId + '"/></td>')
        } else if (data[val].research != null) {
            items = items.concat('<td>' +
            '<a href="#" onclick="getResearchPassword(&#39;' + data[val].research.uuid + '&#39;)" class="btn btn-primary">Password</a>' +
            '&nbsp;' +
            '<a href="' + contextPath + '/downloadFile/' + data[val].research.uuid + '" class="btn btn-primary">Download</a>' +
            '</td>');
        } else {
            items = items.concat('<td></td>');
        }
        items = items.concat(
        '<td></td>' +
        '</tr>\n'
        );
    });
    items = items.concat('</tbody></table>');
    if (!submitted) {
        items = items.concat('<div class="formButtonPanel">');
        items = items.concat('<a href="#" onclick="placeEnquiry()" class="btn btn-primary">Submit</a>');
        items = items.concat('</div>');
    }

    $("#clientEnquiryBidsPanel").html(items);

    globalEnquiryId = enquiryId;

    showMemberBalance();
}

function postEnquiry() {
    var keywords = $("#clientEnquiryKeywords").val();
    var description = $("#clientEnquiryDescription").val();

    address = getAddress();

    var enquiryData = JSON.stringify({
                                      'address': address,
                                      'keywords': keywords,
                                      'description': description
                                     });
    var path = contextPath + '/enquiry';
    $.ajax({
        url: path,
        type: 'post',
        data: enquiryData,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            $("#clientEnquiryKeywords").val('');
            $("#clientEnquiryDescription").val('');
            getClientEnquiries();
        },
        error: function(error) {
            handleErrorResponse(path, error);
        }
    });
}

function placeEnquiry() {
    var ind = 0
    var bidId0 = 0
    var expert0 = ''
    var price0 = 0
    var bidId1 = 0
    var expert1 = ''
    var price1 = 0
    var bidId2 = 0
    var expert2 = ''
    var price2 = 0

    for (i = 0; i < enquiryBids.length; i++) {
        enquiryBid = enquiryBids[i]
        var enquiryBidCheckbox = document.getElementById(enquiryBid.enquiryBidCheckboxId);
        if (enquiryBidCheckbox.value == "on") {
            if (ind == 0) {
                bidId0 = enquiryBid.bidId
                expert0 = enquiryBid.expert
                price0 = enquiryBid.price
            } else if (ind == 1) {
                bidId1 = enquiryBid.bidId
                expert1 = enquiryBid.expert
                price1 = enquiryBid.price
            } else if (ind == 2) {
                bidId2 = enquiryBid.bidId
                expert2 = enquiryBid.expert
                price2 = enquiryBid.price
            }
            var path = contextPath + '/enquiry/bid/' + enquiryBid.bidId + '/select';
            $.ajax({
                url: path,
                type: 'PUT',
                error: function(error) {
                    handleErrorResponse(path, error);
                }
            });
        }
        if (ind == 2) {
            break;
        }
        ind++;
    }

    if (ind > 0) {
        $.get(contextPath + '/enquiry/expert/' + address + '/' + globalEnquiryId,
            function(data) {
                contractInstance.placeEnquiry(globalEnquiryId,
                                              data.keywords,
                                              bidId0,
                                              expert0,
                                              price0,
                                              bidId1,
                                              expert1,
                                              price1,
                                              bidId2,
                                              expert2,
                                              price2,
                                              {from: address},
                    function(error, result) {
                        if(error) {
                            console.log(error);
                            if (bidId0 > 0) {
                                $.put(
                                    contextPath + '/enquiry/bid/' + bidId0 + '/unselect'
                                )
                                .fail(function(error) {
                                    handleError(error);
                                });
                            }
                            if (bidId1 > 0) {
                                $.put(
                                    contextPath + '/enquiry/bid/' + bidId1 + '/unselect'
                                )
                                .fail(function(error) {
                                    handleError(error);
                                });
                            }
                            if (bidId2 > 0) {
                                $.put(
                                    contextPath + '/enquiry/bid/' + bidId2 + '/unselect'
                                )
                                .fail(function(error) {
                                    handleError(error);
                                });
                            }
                        } else {
                            getClientEnquiries();
                            if (bidId0 > 0) {
                                confirmTransaction(contextPath + '/enquiry/bid/' + bidId0 + '/confirm',
                                                   result);
                                waitForTransactionToBeMined(result, enquiryBidSelected, bidId0, result);
                            }
                            if (bidId1 > 0) {
                                confirmTransaction(contextPath + '/enquiry/bid/' + bidId1 + '/confirm',
                                                   result);
                                waitForTransactionToBeMined(result, enquiryBidSelected, bidId1, result);
                            }
                            if (bidId2 > 0) {
                                confirmTransaction(contextPath + '/enquiry/bid/' + bidId2 + '/confirm',
                                                   result);
                                waitForTransactionToBeMined(result, enquiryBidSelected, bidId2, result);
                            }
                        }
                    }
                )
            }
        )
        .fail(function(error) {
            handleError(error);
        })
    }
}

function enquiryBidSelected(enquiryBidId, transactionHash) {
    commitTransaction(contextPath + '/enquiry/bid/' + enquiryBidId + '/commit',
                      transactionHash,
                      function(data) {
                          getEnquiryBids(globalEnquiryId);
                          showMemberBalance();
                      }
    );
}

function getEnquiryBids(enquiryId) {
    $.get(contextPath + '/enquiry/' + enquiryId + '/bid',
        function(data) {
            showEnquiryBids(enquiryId, data);
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function searchResearchItems() {
    address = getAddress()
    keywords = $("#clientResearchKeywords").val()

    if (address != undefined && address != "") {
        $.get({
            url: contextPath + '/research/client/' + address + '/keywords/' + keywords,
            success: function(data) {
                showClientResearchItems(data)
            },
            error: function(error) {
                if (error.responseJSON.status != 404) {
                    handleError(error);
                }
            }
        });
    }
}

function getClientEnquiries() {
    address = getAddress();

    $.get(contextPath + '/enquiry/client/' + address,
        function(data) {
            showClientEnquiries(data)
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}
