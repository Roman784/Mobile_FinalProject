using Backend.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class DecksController : ControllerBase
    {
        private static List<Deck> _decks = new List<Deck>
        {
            new Deck { Id = 1, Name = "Test Deck 1" },
            new Deck { Id = 2, Name = "Test Deck 2" }
        };

        [HttpGet]
        public ActionResult<IEnumerable<Deck>> GetUsers()
        {
            return Ok(_decks);
        }

        [HttpGet("{id}")]
        public ActionResult<Deck> GetUser(int id)
        {
            var user = _decks.FirstOrDefault(u => u.Id == id);

            if (user == null)
            {
                return NotFound();
            }

            return Ok(user);
        }

        [HttpPost]
        public ActionResult<Deck> CreateUser(Deck deck)
        {
            deck.Id = _decks.Max(u => u.Id) + 1;
            _decks.Add(deck);

            return CreatedAtAction(nameof(GetUser), new { id = deck.Id }, deck);
        }

        [HttpPut("{id}")]
        public IActionResult UpdateUser(int id, Deck updatedDeck)
        {
            var user = _decks.FirstOrDefault(u => u.Id == id);

            if (user == null)
            {
                return NotFound();
            }

            user.Name = updatedDeck.Name;

            return NoContent();
        }

        [HttpDelete("{id}")]
        public IActionResult DeleteUser(int id)
        {
            var user = _decks.FirstOrDefault(u => u.Id == id);

            if (user == null)
            {
                return NotFound();
            }

            _decks.Remove(user);
            return NoContent();
        }
    }
}
