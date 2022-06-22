using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public interface IConversationIDListRepositories
    {

        IEnumerable<ConversationIDList> GetConversationIDList(string id);

        void AddMessages(ConversationIDList conversationIDList);

    }
}
