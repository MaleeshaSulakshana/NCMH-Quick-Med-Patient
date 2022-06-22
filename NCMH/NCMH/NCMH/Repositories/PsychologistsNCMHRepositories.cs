using Microsoft.EntityFrameworkCore;
using NCMH.Contexts;
using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public class PsychologistsNCMHRepositories : IPsychologistsNCMHRepositories
    {

        protected ModelsDbContext _context;

        public PsychologistsNCMHRepositories(ModelsDbContext context)
        {
            _context = context;
        }

        public PsychologistsNCMH GetPsychologist(string id)
        {
            return _context.PsychologistsNCMH.AsNoTracking().FirstOrDefault(c => c.Psychologist_ID == id);
        }

        public IEnumerable<PsychologistsNCMH> GetPsychologists()
        {
            return _context.PsychologistsNCMH.AsNoTracking().ToList();
        }
    }
}
