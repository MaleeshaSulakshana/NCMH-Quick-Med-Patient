using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using NCMH.Models;
using NCMH.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Controllers
{
    [Route("api/patients")]
    [ApiController]
    public class PatientsController : ControllerBase
    {

        private readonly IPatientsNCMHRepositories _patientsNCMHRepositories;

        public PatientsController(IPatientsNCMHRepositories patientsNCMHRepositories)
        {
            _patientsNCMHRepositories = patientsNCMHRepositories;
        }

        [HttpGet("{id}")]
        public ActionResult<PatientsNCMH> GetPatient(string id)
        {
            var patient = _patientsNCMHRepositories.GetPatient(id);
            if (patient is null) return NotFound(new { message = "Not Found" });
            return patient;
        }

        [HttpPost("")]
        public ActionResult<PatientsNCMH> AddPatient(PatientsNCMH patientsNCMH)
        {
            /*var citiz = _patientsNCMHRepositories.GetPatient(patientsNCMH.Email);
            if (citiz is not null) return Conflict(new { message = "Email exists" });*/

            patientsNCMH.Password = Utils.Utils.PasswordHashing(patientsNCMH.Password);

            _patientsNCMHRepositories.AddPatient(patientsNCMH);
            return Ok(new
            {
                status = "success",
                msg = "Registration successful"
            });
        }

        [HttpPut("{id}")]
        public ActionResult<PatientsNCMH> UpdatePatient(string id, PatientsNCMH patientsNCMH)
        {

            var patient = _patientsNCMHRepositories.GetPatient(id);
            if (patient is null) return NotFound(new { message = "Not Found" });

            patient.FirstName = patientsNCMH.FirstName;
            patient.LastName = patientsNCMH.LastName;
            patient.DOB = patientsNCMH.DOB;
            patient.Gender = patientsNCMH.Gender;

            _patientsNCMHRepositories.UpdatePatient(patient);
            return Ok(new
            {
                status = "success",
                msg = "Update successful"
            });
        }

        [HttpPut("psw/{id}")]
        public ActionResult<PatientsNCMH> UpdatePatientPsw(string id, PatientsNCMH patientsNCMH)
        {
            var patient = _patientsNCMHRepositories.GetPatient(id);
            if (patient is null) return NotFound(new { message = "Not Found" });

            patient.Password = Utils.Utils.PasswordHashing(patientsNCMH.Password);

            _patientsNCMHRepositories.UpdatePatientPsw(patient);
            return Ok(new
            {
                status = "success",
                msg = "Update successful"
            });
        }

        [HttpPost("login")]
        public ActionResult<PatientsNCMH> CitizenLogin(PatientsNCMH patientsNCMH)
        {
            patientsNCMH.Password = Utils.Utils.PasswordHashing(patientsNCMH.Password);

            var patient = _patientsNCMHRepositories.Login(patientsNCMH);
            if (patient is null) return NotFound(new {
                status = "error",
                msg = "Check email and password!"
            });

            return Ok(new
            {
                id = patient.Patients_ID,
                status = "success",
                msg = "Authentication successfull"
            });

        }

    }
}
