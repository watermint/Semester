var Admin = {
  displayChatDetail: function(h) {
    $("#chat-detail").html(h)
  },
  displayChatLoading: false,
  displayChatList: function(j) {
    var badge = $("#chat-list-badge")
    var chatList = $("#chat-list")
    if (j.loading) {
      badge.empty()
      if (!Admin.displayChatLoading) {
        chatList.html('<div class="list-group-item"><br/><div class="progress progress-striped active"><div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">Loading...</div></div></div>')
        Admin.displayChatLoading = true
      }
      setTimeout(Admin.updateChatList, 5000)
    } else {
      Admin.displayChatLoading = false
      badge.text(j.list.length)
      chatList.empty()
      chatList.append(jQuery.map(j.list, function (c) {
        var b = c.indexed ? '<span class="badge"><span class="glyphicon glyphicon-ok"></span></span>' : ''
        var a = $('<a data-roomid="' + c.id + '" class="list-group-item">' + c.title + b + '</a>')
        a.click(function () {
          var roomId = $(this).attr("data-roomid")
          $.ajax({
            type: "post",
            url: "/admin/chat_detail/" + roomId,
            success: Admin.displayChatDetail
          })
        })
        return a
      }))
    }
  },
  updateChatList: function() {
    $.ajax({
      type: "post",
      url: "/admin/chat_list.json",
      dataType: "json",
      success: Admin.displayChatList
    })
  },
  roomSearch: function(t) {
    $.ajax({
      method: "post",
      data: {"q": t.target.value},
      url: "/admin/chat_list.json",
      dataType: "json",
      success: Admin.displayChatList
    })
  }
}

$(document).ready(function () {
  Admin.updateChatList()
  $('#room-search-box').keyup(function(e){Admin.roomSearch(e)})
})
