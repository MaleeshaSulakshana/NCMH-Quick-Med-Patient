using Microsoft.EntityFrameworkCore;
using NCMH.Contexts;
using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public class PatientsNCMHRepositories : IPatientsNCMHRepositories
    {

        protected ModelsDbContext _context;

        public PatientsNCMHRepositories(ModelsDbContext context)
        {
            _context = context;
        }

        public PatientsNCMH GetPatient(string id)
        {
            return _context.PatientsNCMH.AsNoTracking().FirstOrDefault(c => c.Patients_ID == id);
        }

        public void AddPatient(PatientsNCMH patientsNCMH)
        {
            _context.PatientsNCMH.Add(patientsNCMH);
            _context.SaveChanges();
        }

        public void UpdatePatient(PatientsNCMH patientsNCMH)
        {
            _context.Entry(patientsNCMH).State = EntityState.Modified;
            _context.SaveChanges();
        }

        public void UpdatePatientPsw(PatientsNCMH patientsNCMH)
        {
            _context.Entry(patientsNCMH).State = EntityState.Modified;
            _context.SaveChanges();
        }

        public PatientsNCMH Login(PatientsNCMH patientsNCMH)
        {
            return _context.PatientsNCMH.AsNoTracking().FirstOrDefault(c => c.Email == patientsNCMH.Email && c.Password == patientsNCMH.Password);
        }
    }
}
