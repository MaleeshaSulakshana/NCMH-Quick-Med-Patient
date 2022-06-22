using NCMH.Contexts;
using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public class ReportsRepositories : IReportsRepositories
    {

        protected ModelsDbContext _context;

        public ReportsRepositories(ModelsDbContext context)
        {
            _context = context;
        }

        public void AddReport(Reports reports)
        {
            _context.Reports.Add(reports);
            _context.SaveChanges();
        }

        public IEnumerable<Reports> GetReportsList(string id)
        {
            return _context.Reports.Where(c => c.Patients_ID == id).ToList();
        }
    }
}
