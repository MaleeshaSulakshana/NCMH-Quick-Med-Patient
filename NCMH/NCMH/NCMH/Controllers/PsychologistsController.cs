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
    [Route("api/psychologists")]
    [ApiController]
    public class PsychologistsController : ControllerBase
    {

        private readonly IPsychologistsNCMHRepositories _psychologistsNCMHRepositories;

        public PsychologistsController(IPsychologistsNCMHRepositories psychologistsNCMHRepositories)
        {
            _psychologistsNCMHRepositories = psychologistsNCMHRepositories;
        }


        [HttpGet("")]
        public IEnumerable<PsychologistsNCMH> GetPsychologists()
        {
            return _psychologistsNCMHRepositories.GetPsychologists().ToList();
        }

        [HttpGet("{id}")]
        public ActionResult<PsychologistsNCMH> GetPsychologist(string id)
        {
            var psychologists = _psychologistsNCMHRepositories.GetPsychologist(id);
            if (psychologists is null) return NotFound(new { message = "Not Found" });
            return psychologists;
        }

    }
}
