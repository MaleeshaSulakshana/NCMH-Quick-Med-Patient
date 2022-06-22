using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public interface IMessagesRepositories
    {

        IEnumerable<Messages> GetMessagesList(string id);

        void AddMessages(Messages messages);

    }
}
