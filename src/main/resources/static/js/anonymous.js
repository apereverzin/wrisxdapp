function showAnonymousSearchResearchTab() {
    //$("#anonymousResearchItemPanel").html('')
}

function showAnonymousSearchExpertTab() {
    //$("#anonymousExpertPanel").html('')
}

function viewAnonymousResearchItem(uuid) {
    $.get(contextPath + "/research/" + uuid,
        function(data) {
            var text = '<table class="viewItemTable">'
            text = text.concat('<tr><th>Title</th><td>' + data.title + '</td></tr>')
            text = text.concat('<tr><th>Description</th><td>' + data.description + '</td></tr>')
            text = text.concat('<tr><th>Key words</th><td>' + data.keywords + '</td></tr>')
            text = text.concat('<tr><th>Price</th><td>' + data.price + '</td></tr>')
            text = text.concat('<tr><th>MD5</th><td>' + data.checksum + '</td></tr>')
            text = text.concat('<tr><th>Expert</th><td>' + data.expert.name + '</td></tr>')
            text = text.concat('</table>');
            $("#anonymousResearchItemPanel").show();
            $("#anonymousResearchItemPanel").html(text);
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function showAnonymousResearchItems(data) {
    var items = '<table class="listTable">' +
    '<thead><tr>' +
    '<th>Title</th><th>Expert</th><th>Price</th>' +
    '<th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].title + '</td>' +
        '<td>' + data[val].expert.name + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + '<a href="#" onclick="viewAnonymousResearchItem(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">View</a>' + '</td>' +
        '</tr>'
        )
    })
    items.concat('</tbody></table>');
    $("#anonymousResearchItemsPanel").show();
    $("#anonymousResearchItemPanel").hide();
    $("#anonymousResearchItemsPanel").html(items);
    showMemberBalance();
}

function viewAnonymousExpert(address) {
    $.get(contextPath + "/expert/" + address,
        function(data) {
            var text = '<table class="viewItemTable">'
            text = text.concat('<tr><th>Name</th><td>' + data.name + '</td></tr>')
            text = text.concat('<tr><th>Email</th><td>' + data.emailAddress + '</td></tr>')
            text = text.concat('<tr><th>Key words</th><td>' + data.keywords + '</td></tr>')
            text = text.concat('<tr><th>Description</th><td>' + data.description + '</td></tr>')
            text = text.concat('<tr><td></td><td>Researches</td></tr>')
            text = text.concat('</table>');
            $("#anonymousExpertPanel").show();
            $("#anonymousExpertPanel").html(text);
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function showAnonymousExperts(data) {
    var items = '<table class="listTable">' +
    '<thead><tr>' +
    '<th>Name</th><th>Email</th><th>Description</th>' +
    '<th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].name + '</td>' +
        '<td>' + data[val].emailAddress + '</td>' +
        '<td>' + data[val].description + '</td>' +
        '<td>' + '<a href="#" onclick="viewAnonymousExpert(&#39;' + data[val].address + '&#39;)" class="btn btn-primary">View</a>' + '</td>'
        )
    })
    items.concat('</tbody></table>');
    $("#anonymousExpertsPanel").show();
    $("#anonymousExpertsPanel").html(items);
    $("#anonymousExpertPanel").hide();
    showMemberBalance();
}

function anonymousSearchResearchItems() {
    address = getAddress();
    keywords = $("#anonymousResearchKeywords").val();

    $.get(contextPath + "/research/keywords/" + keywords,
        function(data) {
            showAnonymousResearchItems(data);
            $("#anonymousResearchKeywords").val('');
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function anonymousSearchExperts() {
    address = getAddress()
    keywords = $("#anonymousExpertKeywords").val()

    $.get(contextPath + "/expert/keywords/" + keywords,
        function(data) {
            showAnonymousExperts(data)
            $("#anonymousExpertKeywords").val('')
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}
