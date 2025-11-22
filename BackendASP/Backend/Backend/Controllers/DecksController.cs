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
            new Deck { Id = 2, Name = "Test Deck 2" },
            new Deck { Id = 3, Name = "Test Deck 3" },
            new Deck { Id = 4, Name = "Test Deck 4" },
            new Deck { Id = 5, Name = "Test Deck 5" },
            new Deck { Id = 6, Name = "Test Deck 6" },
            new Deck { Id = 7, Name = "Test Deck 7" },
            new Deck { Id = 8, Name = "Test Deck 8" },
            new Deck { Id = 9, Name = "Test Deck 9" },
            new Deck { Id = 10, Name = "Test Deck 10" },
            new Deck { Id = 11, Name = "Test Deck 11" },
            new Deck { Id = 12, Name = "Test Deck 12" }
        };

        [HttpGet]
        public ActionResult<IEnumerable<Deck>> GetDecks()
        {
            return Ok(_decks);
        }

        [HttpGet("{id}")]
        public ActionResult<Deck> GetDeck(int id)
        {
            var deck = _decks.FirstOrDefault(u => u.Id == id);

            if (deck == null)
            {
                return NotFound();
            }

            return Ok(deck);
        }

        [HttpPost]
        public ActionResult<Deck> CreateDeck(Deck deck)
        {
            deck.Id = _decks.Max(u => u.Id) + 1;
            _decks.Add(deck);

            return CreatedAtAction(nameof(GetDeck), new { id = deck.Id }, deck);
        }

        [HttpPut("{id}")]
        public IActionResult UpdateDeck(int id, Deck updatedDeck)
        {
            var deck = _decks.FirstOrDefault(u => u.Id == id);

            if (deck == null)
            {
                return NotFound();
            }

            deck.Name = updatedDeck.Name;

            return NoContent();
        }

        [HttpDelete("{id}")]
        public IActionResult DeleteDeck(int id)
        {
            var deck = _decks.FirstOrDefault(u => u.Id == id);

            if (deck == null)
            {
                return NotFound();
            }

            _decks.Remove(deck);
            return NoContent();
        }
    }
}
