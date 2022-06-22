using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Models
{
    /*[Keyless]*/
    public class Reports
    {

        public string Patients_ID { get; set; }

        public string FullName { get; set; }

        public string Date { get; set; }

        public string HowIFeel { get; set; }

        [Key]
        public int id { get; set; }

    }
}
