using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using NCMH.Models;
using NCMH.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Controllers
{
    [Route("api/conversation")]
    [ApiController]
    public class ConversationIdListController : ControllerBase
    {

        private readonly IConversationIDListRepositories _conversationIDListRepositories;

        public ConversationIdListController(IConversationIDListRepositories conversationIDListRepositories)
        {
            _conversationIDListRepositories = conversationIDListRepositories;
        }


        [HttpGet("{id}")]
        public IEnumerable<ConversationIDList> GetConversations(string id)
        {
            return _conversationIDListRepositories.GetConversationIDList(id).ToList();
        }

        [HttpPost("")]
        public ActionResult<ConversationIDList> AddMessages(ConversationIDList conversationIDList)
        {
            _conversationIDListRepositories.AddMessages(conversationIDList);
            return Ok(new
            {
                status = "success",
                msg = "Chat created"
            });
        }

    }
}
