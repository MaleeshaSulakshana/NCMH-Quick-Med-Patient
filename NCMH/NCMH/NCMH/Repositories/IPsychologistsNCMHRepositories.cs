using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public interface IPsychologistsNCMHRepositories
    {

        PsychologistsNCMH GetPsychologist(string id);

        IEnumerable<PsychologistsNCMH> GetPsychologists();

    }
}
