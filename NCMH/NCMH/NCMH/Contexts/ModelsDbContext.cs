using Microsoft.EntityFrameworkCore;
using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Contexts
{
    public class ModelsDbContext : DbContext
    {

        public ModelsDbContext(DbContextOptions options) : base(options)
        {

        }

        public DbSet<PatientsNCMH> PatientsNCMH { get; set; }

        public DbSet<ConversationIDList> ConversationIDList { get; set; }

        public DbSet<PsychologistsNCMH> PsychologistsNCMH { get; set; }

        public DbSet<Messages> Messages { get; set; }

        public DbSet<Reports> Reports { get; set; }

    }
}
