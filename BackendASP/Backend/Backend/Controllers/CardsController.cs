using Backend.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Backend.Controllers
{
    [ApiController]
    [Route("api/[controller]")]

    public class CardsController : ControllerBase
    {
        private readonly DecksContext _context;

        public CardsController(DecksContext context)
        {
            _context = context;
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Card>> GetCard(int id)
        {
            var card = await _context.Cards.FindAsync(id);

            if (card == null)
            {
                return NotFound($"Карточка с ID {id} не найдена");
            }

            return Ok(card);
        }

        [HttpGet("deck/{deckId}")]
        public async Task<ActionResult<IEnumerable<Card>>> GetCardsByDeck(int deckId)
        {
            var cards = await _context.Cards
                .Where(c => c.DeckId == deckId)
                .ToListAsync();

            if (!cards.Any())
            {
                return NotFound($"Карточки для колоды с ID {deckId} не найдены");
            }

            return Ok(cards);
        }

        [HttpPost]
        public async Task<ActionResult<Card>> CreateCard(Card card)
        {
            card.Id = 0;

            _context.Cards.Add(card);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetCard), new { id = card.Id }, card);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateCard(int id, Card updatedCard)
        {
            if (id != updatedCard.Id)
            {
                return BadRequest("ID в пути не совпадает с ID в теле запроса");
            }

            var card = await _context.Cards.FindAsync(id);
            if (card == null)
            {
                return NotFound($"Карточка с ID {id} не найдена");
            }

            card.Term = updatedCard.Term;
            card.Definition = updatedCard.Definition;
            card.DeckId = updatedCard.DeckId;

            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteCard(int id)
        {
            var card = await _context.Cards.FindAsync(id);
            if (card == null)
            {
                return NotFound($"Карточка с ID {id} не найдена");
            }

            _context.Cards.Remove(card);
            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}
