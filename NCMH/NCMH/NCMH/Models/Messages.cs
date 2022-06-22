using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace NCMH.Models
{
    /*[Keyless]*/
    public class Messages
    {

        /*[Key]*/
        public string Sender { get; set; }

        public string Msg { get; set; }

        public string ConvoID { get; set; }

        public string SentTime { get; set; }

        [Key]
        public int id { get; set; }

    }
}
