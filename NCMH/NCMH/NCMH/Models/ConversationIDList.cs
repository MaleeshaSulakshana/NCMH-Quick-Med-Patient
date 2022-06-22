using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Models
{
    /*[Keyless]*/
    public class ConversationIDList
    {

        /*[Key]*/
        public string Patients_ID { get; set; }

        public string Patient_FullName { get; set; }

        public string Psychologist_ID { get; set; }

        public string Psychologist_FullName { get; set; }

        public string ConvoID { get; set; }

        [Key]
        public int id { get; set; }

    }
}
