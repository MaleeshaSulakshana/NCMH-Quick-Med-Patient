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
    [Route("api/messages")]
    [ApiController]
    public class MessagesController : ControllerBase
    {

        private readonly IMessagesRepositories _messagesRepositories;

        public MessagesController(IMessagesRepositories messagesRepositories)
        {
            _messagesRepositories = messagesRepositories;
        }


        [HttpGet("{id}")]
        public IEnumerable<Messages> GetMessages(string id)
        {
            return _messagesRepositories.GetMessagesList(id).ToList();
        }


        [HttpPost("")]
        public ActionResult<Messages> AddMessages(Messages messages)
        {

            Console.WriteLine("************ " + messages.ConvoID);
            Console.WriteLine("************ " + messages.Sender);
            Console.WriteLine("************ " + messages.SentTime);
            Console.WriteLine("************ " + messages.Msg);

            _messagesRepositories.AddMessages(messages);
            return Ok(new
            {
                status = "success",
                msg = "Message send"
            });
        }

    }
}
