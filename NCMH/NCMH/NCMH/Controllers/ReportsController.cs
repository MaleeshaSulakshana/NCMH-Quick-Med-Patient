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
    [Route("api/reports")]
    [ApiController]
    public class ReportsController : ControllerBase
    {

        private readonly IReportsRepositories _reportsRepositories;

        public ReportsController(IReportsRepositories reportsRepositories)
        {
            _reportsRepositories = reportsRepositories;
        }


        [HttpGet("{id}")]
        public IEnumerable<Reports> GetReports(string id)
        {
            return _reportsRepositories.GetReportsList(id).ToList();
        }


        [HttpPost("")]
        public ActionResult<Reports> AddReport(Reports reports)
        {
            _reportsRepositories.AddReport(reports);
            return Ok(new
            {
                status = "success",
                msg = "Report added"
            });
        }

    }
}
