function showAnonymousSearchResearchTab() {
    $("#anonymousResearchItemPanel").html('')
}

function showAnonymousSearchExpertTab() {
    $("#anonymousExpertPanel").html('')
}

function viewAnonymousResearchItem(uuid) {
    $.get(contextPath + "/research/" + uuid,
        function(data) {
            var text = '<table style="width:100%">'
            text = text.concat('<tr><td>Title</td><td>' + data.title + '</td></tr>')
            text = text.concat('<tr><td>Description</td><td>' + data.description + '</td></tr>')
            text = text.concat('<tr><td>Key words</td><td>' + data.keywords + '</td></tr>')
            text = text.concat('<tr><td>Price</th><td>' + data.price + '</td></tr>')
            text = text.concat('<tr><td>MD5</td><td>' + data.checksum + '</td></tr>')
            text = text.concat('<tr><td>Expert</td><td>' + data.expert.name + '</td></tr>')
            text = text.concat('</table>')
            $("#anonymousResearchItemPanel").html(text)
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function showAnonymousResearchItems(data) {
    var items = '<table style="width:100%">' +
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
    items.concat('</tbody></table>')
    $("#anonymousResearchItemsPanel").html(items)
    showMemberBalance()
}

function viewAnonymousExpert(address) {
    $.get(contextPath + "/expert/" + address,
        function(data) {
            var text = '<table>'
            text = text.concat('<tr><td><b>Name</b></td><td>' + data.name + '</td></tr>')
            text = text.concat('<tr><td><b>Email</b></td><td>' + data.emailAddress + '</td></tr>')
            text = text.concat('<tr><td><b>Key words</b></td><td>' + data.keywords + '</td></tr>')
            text = text.concat('<tr><td><b>Description</b></td><td>' + data.description + '</td></tr>')
            text = text.concat('<tr><td></td><td>Researches</td></tr>')
            text = text.concat('</table>')
            $("#anonymousExpertPanel").html(text)
        }
    )
    .fail(function(error) {
        handleError(error);
    });
}

function showAnonymousExperts(data) {
    var items = '<table style="width:100%">' +
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
    items.concat('</tbody></table>')
    $("#anonymousExpertsPanel").html(items)
    showMemberBalance()
}

function anonymousSearchResearchItems() {
    address = getAddress()
    keywords = $("#anonymousResearchKeywords").val()

    $.get(contextPath + "/research/keywords/" + keywords,
        function(data) {
            showAnonymousResearchItems(data)
            $("#anonymousResearchKeywords").val('')
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
