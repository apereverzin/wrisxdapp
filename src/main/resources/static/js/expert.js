function expertEnquiriesTabClicked() {
    searchEnquiries()
    showMemberData()
    showMemberBalance()
}

function expertResearchItemsTabClicked() {
    getExpertResearchItems()
    showMemberData()
    showMemberBalance()
    $("#expertResearchItemPanel").html('')
}

function expertPurchasesTabClicked() {
    getExpertPurchases()
    showMemberData()
    showMemberBalance()
}

function expertBidsTabClicked() {
    getExpertEnquiryBids()
    showMemberData()
    showMemberBalance()
}

function registerExpert() {
    address = getAddress();
    var name = $("#expertName").val();
    var emailAddress = $("#expertEmailAddress").val();
    var keywords = $("#expertKeywords").val();
    var description = $("#expertDescription").val();
    var secret = getSecret();

    var expertRequestData = JSON.stringify({
                                            'name': name,
                                            'address': address,
                                            'emailAddress': emailAddress,
                                            'keywords': keywords,
                                            'description': description,
                                            'secret': secret
                                            });

    $.ajax({
        url: contextPath + '/expert',
        type: 'POST',
        data: expertRequestData,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            contractInstance.registerExpert(secret, {from: address},
                function(error, result) {
                    if(error) {
                        rollbackTransaction(contextPath + "/expert/" + address,
                            error,
                            function(data) {
                                showMemberData();
                                showMemberBalance();
                            }
                        );
                    } else {
                        confirmTransaction(contextPath + '/expert/' + address + '/confirm',
                                           result);
                        $("#expertName").val('');
                        $("#expertEmailAddress").val('');
                        $("#expertKeywords").val('');
                        $("#expertDescription").val('');
                        waitForTransactionToBeMined(result, expertRegistered, result)
                    }
                }
            )
        },
        error: function(error) {
            handleError(error);
        }
    });
}

function expertRegistered(transactionHash) {
    commitTransaction(contextPath + '/expert/' + address + '/commit',
                      transactionHash,
                      function(data) {
                          showMemberData();
                          showMemberBalance();
                          showExpertRoleTab();
                      }
    );
}

function depositResearch() {
    address = getAddress();

    var price = $("#expertResearchPrice").val();
    var title = $("#expertResearchTitle").val();
    var description = $("#expertResearchDescription").val();
    var keywords = $("#expertResearchKeywords").val();

    address = getAddress();

    var researchFile = uploadResearchFile();

    var researchRequestData = JSON.stringify({
                                              'expertAddress': address,
                                              'uuid': researchFile.uuid,
                                              'price': price,
                                              'title': title,
                                              'description': description,
                                              'keywords': keywords,
                                              'checksum': researchFile.zipFileChecksumMD5,
                                              'password': researchFile.password,
                                              'clientAddress': 0,
                                              'enquiryId': 0,
                                              'bidId': 0
                                             });

    var path = contextPath + '/research';
    $.ajax({
        url: path,
        type: 'POST',
        data: researchRequestData,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            contractInstance.depositResearch(price,
                                             researchFile.uuid,
                                             researchFile.password,
                                             researchFile.zipFileChecksumMD5,

                                             0,
                                             0,
                                             0,
                                             {from: address},
                function(error, result) {
                    if(error) {
                        rollbackTransaction(contextPath + "/research/" + researchFile.uuid,
                            error,
                            function(data) {
                                getExpertResearchItems();
                                showMemberData();
                                showMemberBalance();
                            }
                        );
                    } else {
                        confirmTransaction(contextPath + '/research/' + researchFile.uuid + '/confirm',
                                           result);
                        $("#expertResearchPrice").val('');
                        $("#expertResearchTitle").val('');
                        $("#expertResearchDescription").val('');
                        $("#expertResearchKeywords").val('');
                        $("#expertResearch-upload-file-input").val('');
                        waitForTransactionToBeMined(result, researchDeposited, researchFile.uuid, result);
                    }
                }
            );
        },
        error: function(error) {
            handleErrorResponse(path, error);
        }
    });
}

function researchDeposited(uuid, transactionHash) {
    commitTransaction(contextPath + '/research/' + uuid + '/commit',
                      transactionHash,
                      function(data) {
                          getExpertResearchItems();
                      }
    );
    showMemberBalance();
}

function depositEnquiryBidResearch(clientAddress, enquiryId, bidId) {
    address = getAddress();

    var price = $("#expertBidPrice").val();
    var title = $("#expertBidTitle").val();
    var description = $("#expertBidDescription").val();
    var keywords = $("#expertBidKeywords").val();

    address = getAddress();

    var researchFile = uploadBidFile();

    var researchRequestData = JSON.stringify({
                                              'expertAddress': address,
                                              'uuid': researchFile.uuid,
                                              'price': price,
                                              'title': title,
                                              'description': description,
                                              'keywords': keywords,
                                              'checksum': researchFile.zipFileChecksumMD5,
                                              'password': researchFile.password,
                                              'clientAddress': clientAddress,
                                              'enquiryId': enquiryId,
                                              'bidId': bidId
                                             });

    var path = contextPath + '/research';
    $.ajax({
        url: path,
        type: 'POST',
        data: researchRequestData,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            contractInstance.depositResearch(price,
                                             researchFile.uuid,
                                             researchFile.password,
                                             researchFile.zipFileChecksumMD5,

                                             clientAddress,
                                             enquiryId,
                                             bidId,
                                             {from: address},
                function(error, result) {
                    if(error) {
                        rollbackTransaction(contextPath + "/research/" + researchFile.uuid,
                            error,
                            function(data) {
                                getExpertEnquiryBids();
                                showMemberData();
                                showMemberBalance();
                            }
                        );
                    } else {
                        confirmTransaction(contextPath + '/research/' + researchFile.uuid + '/confirm',
                                           result);
                        $("#expertBidPrice").val('');
                        $("#expertBidTitle").val('');
                        $("#expertBidDescription").val('');
                        $("#expertBidKeywords").val('');
                        $("#bid-upload-file-input").val('');
                        waitForTransactionToBeMined(result, enquiryBidResearchDeposited, researchFile.uuid, result);
                    }
                }
            );
        },
        error: function(error) {
            handleErrorResponse(path, error);
        }
    });
}

function enquiryBidResearchDeposited(uuid, transactionHash) {
    commitTransaction(contextPath + '/research/' + uuid + '/commit',
                      transactionHash,
                      function(data) {
                          getExpertEnquiryBids();
                          getExpertResearchItems();
                      }
    );
    showMemberBalance();
}

function uploadBidFile() {
    var res;
    var formData = new FormData($("#bid-upload-file-form")[0]);
    $.ajax({
        url: contextPath + '/uploadFile',
        type: 'POST',
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        async: false,
        success: function (data) {
            $("#bid-upload-file-message").text("File succesfully uploaded");
            res = data;
        },
        error: function () {
            $("#bid-upload-file-message").text("File not uploaded (perhaps it's too much big)");
        }
    });
    return res;
}

function uploadResearchFile() {
    var res;
    var formData = new FormData($("#research-upload-file-form")[0]);
    $.ajax({
        url: contextPath + '/uploadFile',
        type: 'POST',
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        async: false,
        success: function (data) {
            $("#expertResearch-upload-file-message").text("File succesfully uploaded");
            res = data;
        },
        error: function () {
            $("#expertResearch-upload-file-message").text("File not uploaded (perhaps it's too much big)");
        }
    });
    return res;
}

function showExpertResearchItems(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>\n' +
    '<th>Title</th>\n' +
    '<th>Price</th>\n' +
    '<th></th><th></th>\n' +
    '</tr></thead>\n' +
    '<tbody>';
    $.each(data, function(val) {
        items = items.concat(
        '<tr>\n' +
        '<td>' + data[val].title + '</td>\n' +
        '<td>' + data[val].price + '</td>\n' +
        '<td>' + '<a href="#" onclick="viewExpertResearchItem(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">View</a>' + '</td>\n' +
        '<td>' + '<a href="#" onclick="getResearchPurchases(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">Purchases</a>' + '</td>\n' +
        '</tr>\n'
        );
    });
    items.concat('</tbody></table>\n');

    $("#expertResearchItemsPanel").html(items);

    showMemberBalance();
}

function showExpertEnquiryBids(data) {
    var items = '<table style="width:100%">\n' +
    '<thead><tr>\n' +
    '<th>Key words</th>\n' +
    '<th>Comment</th>\n' +
    '<th>Price</th>\n' +
    '<th>Expert comment</th>\n' +
    '<th>Selected</th>\n' +
    '<th></th>\n' +
    '</tr></thead>\n' +
    '<tbody>\n';
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].researchEnquiry.keywords + '</td>\n' +
        '<td>' + data[val].researchEnquiry.description + '</td>\n' +
        '<td>' + data[val].price + '</td>\n' +
        '<td>' + data[val].comment + '</td>' +
        '<td>' + data[val].selected + '</td>\n');
        if (data[val].selected && data[val].research == null) {
            items = items.concat('<td>' + '<a href="#" onclick="depositEnquiryBidResearch(&#39;' + data[val].researchEnquiry.client.address + '&#39;,&#39;' + data[val].researchEnquiry.id + '&#39;,&#39;' + data[val].id + '&#39;)" class="btn btn-primary">Deposit research</a>' + '</td>');
        } else if (data[val].research != null) {
            items = items.concat('<td>Deposited</td>');
        } else {
            items = items.concat('<td></td>');
        }
        items = items.concat('</tr>\n');
    });
    items = items.concat('</tbody></table>');
    $("#expertEnquiryBidsPanel").html(items);
    showMemberBalance();
}

function showExpertPurchases(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Title</th><th>Key words</th><th>Price</th><th>Client</th><th>Date</th>\n' +
    '</tr></thead>' +
    '<tbody>';
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].research.title + '</td>' +
        '<td>' + data[val].research.keywords + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + data[val].client.name + '</td>' +
        '<td>' + showDateTime(data[val].timestamp) + '</td>');
        items = items.concat('</tr>\n');
    });
    items = items.concat('</tbody></table>');

    $("#expertPurchasesPanel").html(items);

    showMemberBalance();
}

function viewExpertResearchItem(uuid) {
    $.get(contextPath + "/research/" + uuid,
        function(data) {
            var text = '<table class="itemView">';
            text = text.concat('<tr><th>Title</th><td>' + data.title + '</td></tr>');
            text = text.concat('<tr><th>Description</th><td>' + data.description + '</td></tr>');
            text = text.concat('<tr><th>Key words</th><td>' + data.keywords + '</td></tr>');
            text = text.concat('<tr><th>Price</th><td>' + data.price + '</td></tr>');
            text = text.concat('<tr><th>MD5</th><td>' + data.checksum + '</td></tr>');
            text = text.concat('<tr><th>Expert</th><td>' + data.expert.name + '</td></tr>');
            text = text.concat('</table>');
            $("#expertResearchItemPanel").html(text);
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function showExpertEnquiries(data) {
    var enquiriesExist = false;

    var items =
    '<table style="width:100%">\n' +
    '<thead><tr>\n' +
    '<th>Key words</th>\n' +
    '<th>Description</th>\n' +
    '<th>Your bid</th>\n' +
    '<th></th>\n' +
    '</tr></thead>\n' +
    '<tbody>\n';

    $.each(data, function(val) {
        enquiriesExist = true
        items = items.concat(
            '<tr>' +
            '<td>' + data[val].keywords + '</td>' +
            '<td>' + data[val].description + '</td>'
        );
        if (data[val].enquiryBid == null) {
            items = items.concat('<td>-</td>');
        } else {
            items = items.concat(
                '<td>' + data[val].enquiryBid.price + ' WRX,&nbsp;' + data[val].enquiryBid.comment + '</td>'
            );
        }
        items = items.concat(
            '<td>' + '<a href="#" onclick="viewExpertEnquiry(&#39;' + data[val].id + '&#39;)" class="btn btn-primary">View</a>' + '</td>'
        );
        items = items.concat('</tr>\n');
    })

    items = items.concat('</tbody></table>')

    $("#expertEnquiriesPanel").html(items);
    showMemberBalance();
}

function viewExpertEnquiry(enquiryId) {
    address = getAddress();
    $.get(contextPath + "/enquiry/expert/" + address + "/" + enquiryId,
        function(data) {
            var text = '<table class="itemView">\n';
            text = text.concat('<tr><th class="inputTitle">Key words</th>\n');
            text = text.concat('<td class="inputText">' + data.keywords + '</td></tr>\n');
            text = text.concat('<tr><th class="inputTitle">Description</th>\n')
            text = text.concat('<td class="inputText">' + data.description + '</td></tr>\n');
            text = text.concat('</table>\n');
            if (data.enquiryBid == null) {
                text = text.concat('<table class="itemView">\n');
                text = text.concat('<tr><th class="inputTitle">Bid (WRX)</th>\n');
                text = text.concat('<td><input class="inputText textDecoration" type="text" id="enquiryBid"/></td></tr>\n');
                text = text.concat('<tr><th class="inputTitle">Comment</th>\n');
                text = text.concat('<td><input class="inputText textDecoration" type="text" id="enquiryBidComment"/></td></tr>\n');
                text = text.concat('<tr><td><a href="#" onclick="placeBid(&#39;' + data.id + '&#39;)" class="btn btn-primary">Place bid</a></td></tr>\n');
                text = text.concat('</table>\n');
            }
            $("#expertEnquiryPanel").html(text);
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function placeBid(enquiryId) {
    address = getAddress();
    var enquiryBid = $("#enquiryBid").val();
    var comment = $("#enquiryBidComment").val();

    $.post(contextPath + "/enquiry/" + enquiryId + "/bid",
        {
            'address': address,
            'bid': enquiryBid,
            'comment': comment
        },
        function() {
            searchEnquiries();
            viewExpertEnquiry(enquiryId);
        }
    )
    .fail(function(error) {
        handleError(error);
    });

    $("#enquiryBid").val('')
    $("#enquiryBidComment").val('')
}

function getExpertResearchItems() {
    address = getAddress()
    $.get(contextPath + "/research/expert/" + address,
        function(data) {
            showExpertResearchItems(data)
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function getExpertEnquiryBids() {
    address = getAddress()
    $.get(contextPath + "/enquiry/bid/expert/" + address,
        function(data) {
            showExpertEnquiryBids(data)
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function getExpertPurchases() {
    address = getAddress()
    $.get(contextPath + "/purchase/expert/" + address,
        function(data) {
            showExpertPurchases(data)
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function getResearchPurchases(uuid) {
    address = getAddress()
    $.get(contextPath + "/purchase/research/" + uuid,
        function(data) {
            showExpertPurchases(data)
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function searchEnquiries() {
    address = getAddress()
    keywords = $("#expertEnquiryKeywords").val()

    if (address != undefined && address != "") {
        $.get({
            url: contextPath + "/enquiry/expert/"  + address + "/keywords/" + keywords,
            success: function(data) {
                showExpertEnquiries(data);
            },
            error: function(error) {
                if (error.responseJSON.status != 404) {
                    handleError(error);
                }
            }
        });
    }
}
