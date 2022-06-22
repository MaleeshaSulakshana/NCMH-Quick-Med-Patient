using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Models
{
    public class PsychologistsNCMH
    {

        [Key]
        public string Psychologist_ID { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string NIC { get; set; }

        public string Gender { get; set; }

        public string Qualifications { get; set; }

        public string Description { get; set; }

        public string Email { get; set; }

        public string Password { get; set; }

        public string Account_Status { get; set; }

    }
}
