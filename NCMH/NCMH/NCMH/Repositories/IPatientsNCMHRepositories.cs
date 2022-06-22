using NCMH.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Repositories
{
    public interface IPatientsNCMHRepositories
    {

        PatientsNCMH GetPatient(string id);

        PatientsNCMH Login(PatientsNCMH patientsNCMH);

        void AddPatient(PatientsNCMH patientsNCMH);

        void UpdatePatient(PatientsNCMH patientsNCMH);

        void UpdatePatientPsw(PatientsNCMH patientsNCMH);

    }
}
