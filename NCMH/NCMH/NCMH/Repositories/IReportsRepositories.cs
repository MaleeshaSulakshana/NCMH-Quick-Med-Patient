using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public interface IReportsRepositories
    {

        IEnumerable<Reports> GetReportsList(string id);

        void AddReport(Reports reports);

    }
}
