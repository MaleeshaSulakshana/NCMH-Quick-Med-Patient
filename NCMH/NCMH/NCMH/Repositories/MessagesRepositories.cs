using NCMH.Contexts;
using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public class MessagesRepositories : IMessagesRepositories
    {

        protected ModelsDbContext _context;

        public MessagesRepositories(ModelsDbContext context)
        {
            _context = context;
        }

        public void AddMessages(Messages messages)
        {
            _context.Messages.Add(messages);
            _context.SaveChanges();
        }

        public IEnumerable<Messages> GetMessagesList(string id)
        {
            return _context.Messages.Where(c => c.ConvoID == id).OrderByDescending(m => m.ConvoID).ToList();
        }
    }
}
