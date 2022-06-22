using NCMH.Contexts;
using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public class ConversationIDListRepositories : IConversationIDListRepositories
    {

        protected ModelsDbContext _context;

        public ConversationIDListRepositories(ModelsDbContext context)
        {
            _context = context;
        }

        public void AddMessages(ConversationIDList conversationIDList)
        {
            _context.ConversationIDList.Add(conversationIDList);
            _context.SaveChanges();
        }

        public IEnumerable<ConversationIDList> GetConversationIDList(string id)
        {
            return _context.ConversationIDList.Where(c => c.Patients_ID == id).ToList();
        }
    }
}
