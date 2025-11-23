using Backend.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Backend.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class DecksController : ControllerBase
    {
        private readonly DecksContext _context;

        public DecksController(DecksContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Deck>>> GetDecks()
        {
            var decks = await _context.Decks.ToListAsync();
            return Ok(decks);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Deck>> GetDeck(int id)
        {
            var deck = await _context.Decks.FindAsync(id);

            if (deck == null)
            {
                return NotFound($"Колода с ID {id} не найдена");
            }

            return Ok(deck);
        }

        [HttpPost]
        public async Task<ActionResult<Deck>> CreateDeck(Deck deck)
        {
            deck.Id = 0;

            _context.Decks.Add(deck);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetDeck), new { id = deck.Id }, deck);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateDeck(int id, Deck updatedDeck)
        {
            if (id != updatedDeck.Id)
            {
                return BadRequest("ID в пути не совпадает с ID в теле запроса");
            }

            var deck = await _context.Decks.FindAsync(id);
            if (deck == null)
            {
                return NotFound($"Колода с ID {id} не найдена");
            }

            deck.Name = updatedDeck.Name;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DeckExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDeck(int id)
        {
            var deck = await _context.Decks.FindAsync(id);
            if (deck == null)
            {
                return NotFound($"Колода с ID {id} не найдена");
            }

            _context.Decks.Remove(deck);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool DeckExists(int id)
        {
            return _context.Decks.Any(e => e.Id == id);
        }
    }
}
